import React, { useState } from "react";
import { toast } from "react-toastify"; // Eklemeyi unutma

export default function UnifiedForm({
  dbConfig,
  setDbConfig,
  dataObjects,
  selectedDataObject,
  setSelectedDataObject,
  chartTypes,
  selectedChartType,
  setSelectedChartType,
  mapping,
  setMapping,
  onTestConnection,
}) {
  const [connectionStatus, setConnectionStatus] = useState(null);
  const [loading, setLoading] = useState(false);

 
  const selectedObjectColumns =
    dataObjects.find((obj) => obj.name === selectedDataObject)?.columns || [];

  const handleDbChange = (e) => {
    setDbConfig((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleMappingChange = (e) => {
    setMapping((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

const handleTestConnection = async () => {
  setLoading(true);
  setConnectionStatus(null);

  try {
    const success = await onTestConnection(dbConfig);
    if (success) {
      toast.success("Database connection successful!", {
        position: "top-right",
        autoClose: 3000,
        theme: "colored",
      });
      setConnectionStatus("success");
    } else {
      toast.error("Connection failed. Please check credentials.", {
        position: "top-right",
        autoClose: 4000,
        theme: "colored",
      });
      setConnectionStatus("error");
    }
  } catch (error) {
    console.error("Test Connection Error:", error);
    toast.error(
      ` Unexpected error: ${error?.message || "Unknown issue occurred."}`,
      {
        position: "top-right",
        autoClose: 5000,
        theme: "colored",
      }
    );
    setConnectionStatus("error");
  } finally {
    setLoading(false);
  }
};

  return (
    <>
      <p className="mb-6 text-gray-700">
        Bu formda, veritabanı bağlantı bilgilerinizi girip bağlantıyı test edebilirsiniz. 
        Ardından, veritabanından gelen veri nesnelerinden birini seçip, grafik için X ve Y eksen alanlarını grafik de görebilirsiniz.
      </p>

      <section>
        <h2 className="text-2xl font-semibold mb-4 text-gray-900">
          Veritabanı Bağlantısı
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-5 gap-4 mb-4">
          {["host", "port", "database", "username", "password"].map((field) => (
            <input
              key={field}
              type={field === "password" ? "password" : "text"}
              name={field}
              placeholder={field.charAt(0).toUpperCase() + field.slice(1)}
              value={dbConfig[field]}
              onChange={handleDbChange}
              className="p-3 rounded border border-gray-300 bg-white text-gray-900 focus:outline-none focus:ring-2 focus:ring-red-400"
              required
            />
          ))}
        </div>

        <div className="flex items-center gap-4 mb-8">
          <button
            type="button"
            onClick={handleTestConnection}
            disabled={loading}
            className="bg-default hover:bg-black text-white font-semibold px-6 py-2 rounded-lg transition disabled:opacity-60"
          >
            {loading ? "Test Ediliyor..." : "Bağlantıyı Test Et"}
          </button>

          {connectionStatus === "success" && (
            <span className="text-green-600 font-medium">✅ Bağlantı başarılı</span>
          )}
          {connectionStatus === "error" && (
            <span className="text-red-600 font-medium">❌ Bağlantı başarısız</span>
          )}
        </div>
      </section>

      {dataObjects.length > 0 && (
        <div className="overflow-x-auto mt-4 border border-gray-200 rounded-lg">
          <table className="min-w-full text-left text-sm text-gray-700">
            <thead className="bg-gray-100 text-gray-900">
              <tr>
                <th className="px-4 py-2">İsim</th>
                <th className="px-4 py-2">Tip</th>
                <th className="px-4 py-2">Sütunlar</th>
              </tr>
            </thead>
            <tbody>
              {dataObjects.map((obj, idx) => (
                <tr key={obj.name || `row-${idx}`} className="border-t border-gray-200 hover:bg-gray-50">
                  <td className="px-4 py-2 font-medium">{obj.name}</td>
                  <td className="px-4 py-2">{obj.type}</td>
                  <td className="px-4 py-2">{obj.columns?.join(", ") || "-"}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      <section>
        <h2 className="text-xl font-semibold mb-1 text-gray-900">Veri Objesi Seçimi</h2>
        <select
          value={selectedDataObject}
          onChange={(e) => {
            setSelectedDataObject(e.target.value);
            // Seçim değişince mapping sıfırlanabilir
            setMapping({ xAxis: "", yAxis: "" });
          }}
          className="w-full p-3 rounded border border-gray-300 bg-white text-gray-900 focus:outline-none focus:ring-2 focus:ring-red-400 mb-4"
          required
        >
          <option value="" disabled>
            Seçiniz
          </option>
          {dataObjects.map((obj) => (
            <option key={obj.name} value={obj.name}>
              {obj.name}
            </option>
          ))}
        </select>
      </section>

      {selectedObjectColumns.length > 0 && (
        <section>
          <h2 className="text-xl font-semibold mb-1 text-gray-900">Veri Haritalama</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-2 mb-4">
            <select
              name="xAxis"
              value={mapping.xAxis}
              onChange={handleMappingChange}
              className="p-3 rounded border border-gray-300 bg-white text-gray-900 focus:outline-none focus:ring-2 focus:ring-red-400"
              required
            >
              <option value="" disabled>
                X Axis için sütun seçin
              </option>
              {selectedObjectColumns.map((col) => (
                <option key={col} value={col}>
                  {col}
                </option>
              ))}
            </select>

            <select
              name="yAxis"
              value={mapping.yAxis}
              onChange={handleMappingChange}
              className="p-3 rounded border border-gray-300 bg-white text-gray-900 focus:outline-none focus:ring-2 focus:ring-red-400"
              required
            >
              <option value="" disabled>
                Y Axis için sütun seçin
              </option>
              {selectedObjectColumns.map((col) => (
                <option key={col} value={col}>
                  {col}
                </option>
              ))}
            </select>
          </div>
        </section>
      )}
    </>
  );
}
