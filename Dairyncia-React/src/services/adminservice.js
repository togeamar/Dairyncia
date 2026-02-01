import { ADMIN_BASE_URL, API_BASE_URL } from "../../constants/ApiConstants";
import client from "./client";

export function loadRates({milkType,pageNumber=1,pageSize=50}){
    return client.get(`${API_BASE_URL}/milk-rate/getallrates`,{params:{
        pageNumber,
        pageSize,
        milkType
        }
    });
}

export function uploadfFile(formData, milkType) {
    return client.post(`${API_BASE_URL}/milk-rate/upload`, formData, {
        params: { milkType }, 
        headers: { "Content-Type": "multipart/form-data" }
    });
}