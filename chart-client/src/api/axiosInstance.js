import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

// İstek öncesi interceptor ile token'ı ekle
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("accessToken"); // localStorage'dan token al
    if (token) {
      config.headers.Authorization = `Bearer ${token}`; // Authorization header'a ekle
    }
    return config;
  },
  (error) => Promise.reject(error)
);

axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        // Refresh token isteği at (refresh token'ı localStorage'dan alabilirsin)
        const refreshToken = localStorage.getItem("refreshToken");
        if (!refreshToken) {
          throw new Error("Refresh token yok");
        }
        const res = await axios.post("http://localhost:8080/api/auth/refresh-token", { refreshToken });

        const newAccessToken = res.data.accessToken;
        localStorage.setItem("accessToken", newAccessToken);

        // Yeni token ile orijinal isteği tekrar dene
        originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
        return axiosInstance(originalRequest);
      } catch (refreshErr) {
        console.error("Token yenileme başarısız:", refreshErr);
        window.location.href = "/auth"; // Login sayfasına yönlendir
      }
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;
