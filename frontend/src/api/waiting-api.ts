import { ApiResponse, INoResponseDTO } from "@interface/api";
import axiosInstance from "./axiosInstance";

const path = "/waiting";

//유저의 예약하기
export const reserveTruckWaiting = (foodTruckId: number): ApiResponse<INoResponseDTO> => {
	return axiosInstance.post(`${path}/foodtrucks/${foodTruckId}`);
};