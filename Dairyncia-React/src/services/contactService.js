import axios from "axios";

const API_BASE_URL = "http://localhost:5225"; 


export const sendContactMessage = (data) => {
  return axios.post(`${API_BASE_URL}/api/contact`, data,{
    headers: {
      'Content-Type': 'application/json',
    },
  });
};
