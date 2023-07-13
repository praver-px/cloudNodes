import axios from "axios";
import qs from "qs";

export const noteBaseRequest = axios.create({
    baseURL: '/note',
    transformRequest: [(data, headers) => {
        return qs.stringify(data)
    }]
})