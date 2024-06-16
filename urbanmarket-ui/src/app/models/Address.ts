export interface Address {
    id?: number;
    houseNo: string;
    addressLine1: string;
    addressLine2?: string | null;
    city: string;
    state: string;
    pincode: number;
}