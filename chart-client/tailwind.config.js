/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {},
     colors: {
          light: '#FF6B6B',    // Açık kırmızı (hover)
          default: '#E63946',  // Ana kırmızı (buton)
          dark: '#9B1B1B',     // Koyu kırmızı (gradient başlangıcı)
          black: '#1C1C1C',    // Siyah
          white: '#FFFFFF',    // Beyaz
    }
  },
  plugins: [],
}

