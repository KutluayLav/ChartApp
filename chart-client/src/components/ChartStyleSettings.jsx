import React, { useState } from "react";
import ChartAxisSettings from "./ChartAxisSettings"; // Doğru yolu ayarla

const colorThemes = {
  red: {
    backgroundColor: "rgba(230, 57, 70, 0.5)",
    borderColor: "rgba(194, 30, 45, 1)",
  },
  blue: {
    backgroundColor: "rgba(70, 130, 180, 0.5)",
    borderColor: "rgba(25, 50, 120, 1)",
  },
  green: {
    backgroundColor: "rgba(70, 180, 70, 0.5)",
    borderColor: "rgba(20, 120, 20, 1)",
  },
};

const lineStyles = ["solid", "dashed", "dotted"];

const ChevronIcon = ({ isOpen }) => (
  <svg
    className={`w-5 h-5 transition-transform duration-300 ${
      isOpen ? "rotate-180" : "rotate-0"
    }`}
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    viewBox="0 0 24 24"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path strokeLinecap="round" strokeLinejoin="round" d="M19 9l-7 7-7-7"></path>
  </svg>
);

export default function ChartStyleSettings({
  styleConfig,
  setStyleConfig,
  axisConfig,
  setAxisConfig,
  chartTypes,
  selectedChartType,
  setSelectedChartType,
}) {
  const [isOpen, setIsOpen] = useState(false);

  const toggleDropdown = () => setIsOpen((prev) => !prev);

  const handleColorChange = (e) => {
    setStyleConfig((prev) => ({ ...prev, colorTheme: e.target.value }));
  };

  const handleLineWidthChange = (e) => {
    setStyleConfig((prev) => ({ ...prev, lineWidth: Number(e.target.value) }));
  };

  const handleLineStyleChange = (e) => {
    setStyleConfig((prev) => ({ ...prev, lineStyle: e.target.value }));
  };

  return (
    <div className="bg-white rounded shadow-md  w-full">
      {/* Başlık */}
         <button
        onClick={toggleDropdown}
        className="w-full flex justify-between items-center bg-default text-white p-4 font-semibold text-lg border  cursor-pointer focus:outline-none"
        aria-expanded={isOpen}
        aria-controls="chart-style-settings"
      >
        Grafik Ayarları
        <ChevronIcon isOpen={isOpen} />
      </button>

      {/* İçerik */}
      {isOpen && (
        <div id="chart-style-settings" className="p-4 space-y-6 border">
          {/* Grafik Türü Seçimi */}
          <div>
            <h3 className="text-xl font-semibold text-gray-800 mb-4">
              Grafik Türü Seçimi
            </h3>
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              {chartTypes.map((type) => {
                const isSelected = selectedChartType === type;
                return (
                  <button
                    key={type}
                    type="button"
                    onClick={() => setSelectedChartType(type)}
                    className={`w-full px-4 py-2 rounded-lg font-semibold border transition-all duration-200 ${
                      isSelected
                        ? "bg-default text-white border-red-600 shadow-md"
                        : "bg-white text-gray-800 border-gray-300 hover:bg-gray-100"
                    }`}
                  >
                    {type}
                  </button>
                );
              })}
            </div>
          </div>

          {/* Renk ve Çizgi ayarları */}
          <div className="space-y-4">
            <div>
              <label className="block font-medium mb-1">Renk Teması:</label>
              <select
                value={styleConfig.colorTheme}
                onChange={handleColorChange}
                className="w-full p-2 border rounded"
              >
                {Object.keys(colorThemes).map((key) => (
                  <option key={key} value={key}>
                    {key.charAt(0).toUpperCase() + key.slice(1)}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block font-medium mb-1">Çizgi Kalınlığı:</label>
              <input
                type="range"
                min="1"
                max="10"
                value={styleConfig.lineWidth}
                onChange={handleLineWidthChange}
                className="w-full"
              />
              <div>{styleConfig.lineWidth}px</div>
            </div>

            <div>
              <label className="block font-medium mb-1">Çizgi Stili:</label>
              <select
                value={styleConfig.lineStyle}
                onChange={handleLineStyleChange}
                className="w-full p-2 border rounded"
              >
                {lineStyles.map((style) => (
                  <option key={style} value={style}>
                    {style.charAt(0).toUpperCase() + style.slice(1)}
                  </option>
                ))}
              </select>
            </div>
          </div>

          {/* Eksen ayarları */}
          <ChartAxisSettings axisConfig={axisConfig} setAxisConfig={setAxisConfig} />
        </div>
      )}
    </div>
  );
}

export { colorThemes };
