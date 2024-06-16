import { Address } from "./Address";

export interface RequestUserDto {
    name: string;
	email: string;
	password:string;
	role: string;
	dob: Date;
	phone: string;
	gender: string;
	profilePicPath?: string;
	address:any;
}