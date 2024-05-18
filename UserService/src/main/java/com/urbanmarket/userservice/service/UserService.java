/**
 * 09-Apr-2024
 * meeth
 */
package com.urbanmarket.userservice.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.urbanmarket.userservice.dto.RequestAddressDto;
import com.urbanmarket.userservice.dto.RequestCustomerDto;
import com.urbanmarket.userservice.dto.RequestUserCredentialDto;
import com.urbanmarket.userservice.dto.ResponseAddressDto;
import com.urbanmarket.userservice.dto.ResponseCustomerDto;
import com.urbanmarket.userservice.exception.DataNotFoundException;
import com.urbanmarket.userservice.model.Address;
import com.urbanmarket.userservice.model.Customer;
import com.urbanmarket.userservice.openfeign.AuthenticationClient;
import com.urbanmarket.userservice.openfeign.ReviewClient;
import com.urbanmarket.userservice.repository.AddressRepository;
import com.urbanmarket.userservice.repository.CustomerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service layer for customer/user
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {

	private final CustomerRepository customerRepository;

	private final AddressRepository addressRepository;

	private final ModelMapper modelMapper;

	private final MessageSource source;

	private final AuthenticationClient authenticationClient;
	
	private final ReviewClient reviewClient;

	@Value("${server.port}")
	String port;

	/**
	 * Create customer. If password is provided then creates authentication
	 * credential in authentication service
	 * 
	 * @param customerDto
	 */
	public void createCustomer(RequestCustomerDto customerDto) {
		log.info("USERSERVICE: Create customer @PORT: " + port);

		customerRepository.save(requestToModel(customerDto));

		if (customerDto.getPassword() != null) {
			RequestUserCredentialDto credentialDto = RequestUserCredentialDto.builder().email(customerDto.getEmail())
					.role(customerDto.getRole().toString()).password(customerDto.getPassword()).build();

			authenticationClient.saveUser(credentialDto);
		}
	}

	/**
	 * Get all customers
	 * 
	 * @return list of responseCustomerDto
	 */
	public List<ResponseCustomerDto> getAllCustomer() {
		log.info("USERSERVICE: Get all customer @PORT: " + port);
		return customerRepository.findAll().stream().map(this::modelToResponse).toList();
	}

	/**
	 * Get customer by id
	 * 
	 * @param id customerId
	 * @return responseCustomerDto
	 * @throws DataNotFoundException
	 */
	public ResponseCustomerDto getCustomerById(long id) {
		log.info("USERSERVICE: Get customer by Id @PORT: " + port);
		return modelToResponse(getCustomerOrThrow(id));
	}

	/**
	 * Delete customer by id.<br>
	 * <b> Silently ignores if no customer is found </b>
	 * 
	 * @param id customerId.
	 */
	public void deleteCustomer(long id) {
		log.info("USERSERVICE: Delete customer @PORT: " + port);
		Optional<Customer> byId = customerRepository.findById(id);
		if (byId.isPresent()) {
			customerRepository.deleteById(id);
			// delete user credentials
			authenticationClient.deleteUser(byId.get().getEmail());
			// delete all reviews of customer
			reviewClient.deleteReviewOfCustomer(id);
		}
	}

	/**
	 * Update user details. Checks if address already exists. updates/ creates
	 * accordingly
	 * 
	 * @param id          customer id
	 * @param customerDto new customer details
	 * @return updated customer details
	 * @throws DataNotFoundException
	 */
	public ResponseCustomerDto editCustomer(long id, RequestCustomerDto customerDto) {
		log.info("USERSERVICE: Update customer @PORT: " + port);
		Customer customer = getCustomerOrThrow(id);
		// new values
		Customer newCustomer = requestToModel(customerDto);
		if (newCustomer.getAddress() == null) {
			// if no address is provided
			newCustomer.setAddress(customer.getAddress());
		} else {
			Map<String, Address> oldAddresses = customer.getAddress();
			for (Map.Entry<String, Address> add : newCustomer.getAddress().entrySet()) {
				// if new address has same key as old one, update it
				if (oldAddresses.containsKey(add.getKey())) {
					Address existing = oldAddresses.get(add.getKey());
					add.getValue().setId(existing.getId());
				}
			}
		}
		newCustomer.setId(customer.getId());
		return modelToResponse(customerRepository.save(newCustomer));

	}

	/**
	 * Update address.
	 * 
	 * @param addressId  id
	 * @param addressDto requestAddressDto
	 * @return responseAddressDto updated
	 * @throws DataNotFoundException
	 */
	public ResponseAddressDto updateAddress(long addressId, RequestAddressDto addressDto) {
		log.info("USERSERVICE: Update Address @PORT: " + port);
		// check if address exists.
		Address address = addressRepository.findById(addressId)
				.orElseThrow(() -> new DataNotFoundException("No address found with id: " + addressId));
		Address newAddress = requestToModel(addressDto);
		newAddress.setId(address.getId());
		return modelToResponse(addressRepository.save(newAddress));
	}

	/**
	 * Check if address with same name exists. If does update it or else create new.
	 * 
	 * @param customerId id
	 * @param addressDto new address details
	 * @throws DataNotFoundException
	 */
	public void createAddress(long customerId, RequestAddressDto addressDto) {
		log.info("USERSERVICE: Create Address @PORT: " + port);
		// check if customer exists
		Customer customer = getCustomerOrThrow(customerId);
		Map<String, Address> existingAddress = customer.getAddress();
		// if the address of same name exists, update it. Or else create new
		if (existingAddress.containsKey(addressDto.getName())) {
			updateAddress(existingAddress.get(addressDto.getName()).getId(), addressDto);
		} else {
			Address address = requestToModel(addressDto);
			existingAddress.put(addressDto.getName(), address);
			customer.setAddress(existingAddress);
			customerRepository.save(customer);
		}
	}

	/**
	 * Delete an address by customer id and it's name
	 * 
	 * @param customerId id
	 * @param key        address key
	 */
	public void deleteAddress(long customerId, String key) {
		log.info("USERSERVICE: Delete Address @PORT: " + port);
		// check if customer exists
		Customer customer = getCustomerOrThrow(customerId);
		Map<String, Address> existingAddress = customer.getAddress();
		if (existingAddress.containsKey(key)) {
			// remove from customer's map as well as from database
			addressRepository.deleteById(existingAddress.get(key).getId());
			existingAddress.remove(key);
		}
	}

	/**
	 * Add/update profile picture
	 * 
	 * @param id   customer id
	 * @param file profile picture
	 * @throws IOException if image is not saved properly
	 */
	public void addProfilePicture(long id, MultipartFile file) throws IOException {
		Customer customer = getCustomerOrThrow(id);
		customer.setProfilePicPath(saveImage(id, file));
		customerRepository.save(customer);
	}

	/**
	 * Converts requestCustomerDto to customer model
	 * 
	 * @param customerDto requestCustomerDto
	 * @return Customer model
	 */
	private Customer requestToModel(RequestCustomerDto customerDto) {
		return modelMapper.map(customerDto, Customer.class);
	}

	/**
	 * Converts Customer model to responseCustomerDto
	 * 
	 * @param customer model
	 * @return responseCustomerModel
	 */
	private ResponseCustomerDto modelToResponse(Customer customer) {
		return modelMapper.map(customer, ResponseCustomerDto.class);
	}

	/**
	 * Converts requestAddressDto to address model
	 * 
	 * @param addressDto requestAddressDto
	 * @return address model
	 */
	private Address requestToModel(RequestAddressDto addressDto) {
		return modelMapper.map(addressDto, Address.class);
	}

	/**
	 * Converts Address model to responseAddressDto
	 * 
	 * @param address model
	 * @return ResponseAddressDto
	 */
	private ResponseAddressDto modelToResponse(Address address) {
		return modelMapper.map(address, ResponseAddressDto.class);
	}

	/**
	 * @return path of profile picture folder
	 */
	private String getProfilePicFolderPath() {
		return source.getMessage("profileFolder", null, Locale.ENGLISH);
	}

	/**
	 * Saves an image to resources and returns it entire path.<br>
	 * Name consists of customerId for its image.<br>
	 * 
	 * @param customerId imageName
	 * @param file       image
	 * @return imagePath
	 * @throws IOException
	 */
	private String saveImage(long customerId, MultipartFile file) throws IOException {
		// folder's path = static default path
		String folderPath = getProfilePicFolderPath();
		// set file name as customerId + the extension on original file
		String imageName = file.getOriginalFilename();
		String fileName;
		// if in case original file name isn't fetched, use default .jpg
		if (imageName == null) {
			fileName = customerId + ".jpg";
		} else {
			fileName = customerId + "." + imageName.split("[.]")[1];
		}
		// set file path as folderPath + filename
		String filePath = folderPath + File.separator + fileName;
		filePath = filePath.replace('/', File.separator.charAt(0));
		// create file directory if it doesn't exist
		File dir = new File(folderPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// save file
		Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
		return filePath;
	}

	/**
	 * Get customer from database or else throw {@link DataNotFoundException}
	 * 
	 * @param id customer's id
	 * @return customer
	 * @throws DataNotFoundException
	 */
	private Customer getCustomerOrThrow(long id) {
		return customerRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("No customer exists with id: " + id));
	}
}
