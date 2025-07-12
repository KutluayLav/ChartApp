import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import axios from "../api/axiosInstance";

export default function Navbar() {
  const [isOpen, setIsOpen] = useState(false);
  const [userInfo, setUserInfo] = useState(null); // ✅ Kullanıcı bilgisi için state

  const navigate = useNavigate();
  const { logout } = useAuth();

  // ✅ Kullanıcı bilgilerini al
  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await axios.get("/users/me"); // Token header’da otomatik gider
        setUserInfo(response.data.data); // { username, authorities, ... }
      } catch (error) {
        console.error("Failed to fetch user info:", error);
        setUserInfo(null);
      }
    };

    fetchUserInfo();
  }, []);

  const handleLogout = async () => {
    try {
      await axios.post("/auth/logout");
    } catch (error) {
      console.error("Logout failed:", error);
    } finally {
      logout();
      navigate("/auth");
    }
  };

  const username = userInfo?.username || "Kullanıcı";
  const role = userInfo?.authorities?.[0]?.replace("ROLE_", "") || "";

  return (
    <nav className="bg-white shadow-md fixed top-0 left-0 right-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16 items-center">
          {/* Logo */}
          <div className="flex-shrink-0 text-2xl font-bold text-red-600">
            Dashboard
          </div>

          {/* Hamburger Menü (Mobil için) */}
          <div className="flex lg:hidden">
            <button
              onClick={() => setIsOpen(!isOpen)}
              type="button"
              className="inline-flex items-center justify-center p-2 rounded-md text-gray-600 hover:text-red-600 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-red-500"
              aria-controls="mobile-menu"
              aria-expanded={isOpen}
            >
              <span className="sr-only">Menü Aç/Kapa</span>
              {isOpen ? (
                <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                </svg>
              ) : (
                <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                </svg>
              )}
            </button>
          </div>

          {/* Masaüstü Menü */}
          <div className="hidden lg:flex lg:items-center lg:space-x-6">
            <div className="relative group">
              <div className="flex items-center space-x-2 text-gray-700 font-medium hover:text-red-600 cursor-pointer">
                <span className="hidden sm:inline">Hoşgeldin,</span>
                <span className="text-red-600 font-semibold">{username}</span>
                {role && <span className="text-xs font-medium text-gray-500 ml-1">({role})</span>}
                <svg
                  className="w-5 h-5 text-red-600 transition-transform duration-300 transform group-hover:rotate-180"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                </svg>
              </div>

              <div className="absolute right-0 mt-2 w-48 bg-white border rounded-lg shadow-lg opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 z-50">
                <a
                  href="/dashboard"
                  className="block px-4 py-3 text-sm text-gray-700 hover:bg-default hover:text-red-600 transition rounded-t-md"
                >
                  Profil
                </a>
                <button
                  onClick={handleLogout}
                  className="w-full text-left px-4 py-3 text-sm text-gray-700 hover:bg-default hover:text-red-600 transition rounded-b-md"
                >
                  Çıkış Yap
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Mobil Menü */}
      {isOpen && (
        <div className="lg:hidden" id="mobile-menu">
          <div className="px-4 pt-2 pb-3 space-y-1">
            <div className="flex items-center space-x-2 text-red-600 font-medium">
              <span>Hoşgeldin,</span>
              <span className="font-semibold">{username}</span>
              {role && <span className="text-xs text-gray-500">({role})</span>}
            </div>
            <a
              href="/dashboard"
              className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
            >
              Profil
            </a>
            <button
              onClick={handleLogout}
              className="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
            >
              Çıkış Yap
            </button>
          </div>
        </div>
      )}
    </nav>
  );
}
