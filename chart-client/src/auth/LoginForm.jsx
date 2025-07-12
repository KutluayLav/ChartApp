import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import axios from "../api/axiosInstance";

export default function LoginForm() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post("/auth/login", {
        username,
        password,
      });

      const { accessToken, refreshToken } = response.data.data;

      login(accessToken);
      localStorage.setItem("refreshToken", refreshToken);

      navigate("/dashboard");
    } catch (err) {
      alert("Giriş başarısız! Kullanıcı adı veya şifre hatalı.");
      console.error("Login error:", err);
    }
  };

  return (
    <>
      <h2 className="text-3xl font-bold text-center text-[#1C1C1C] mb-6">Giriş Yap</h2>
      <form onSubmit={handleLogin}>
        <label htmlFor="username" className="block text-sm font-medium text-[#1C1C1C] mb-1">
          Kullanıcı Adı
        </label>
        <input
          id="username"
          type="text"
          placeholder="Kullanıcı adınızı girin"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
          className="w-full px-4 py-2 mb-4 border border-[#FF6B6B] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FF6B6B]"
        />

        <label htmlFor="password" className="block text-sm font-medium text-[#1C1C1C] mb-1">
          Şifre
        </label>
        <input
          id="password"
          type="password"
          placeholder="******"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          className="w-full px-4 py-2 mb-6 border border-[#FF6B6B] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FF6B6B]"
        />

        <button
          type="submit"
          className="w-full bg-[#E63946] hover:bg-[#FF6B6B] text-white font-semibold py-2 rounded-lg transition"
        >
          Giriş Yap
        </button>
      </form>
    </>
  );
}
