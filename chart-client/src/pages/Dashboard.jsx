import { useState } from "react";
import ChartDisplay from "../components/ChartDisplay";
import UnifiedForm from "../components/UnifiedForm";
import Navbar from "../components/Navbar";
import ChartStyleSettings, { colorThemes } from "../components/ChartStyleSettings";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import axiosInstance from "../api/axiosInstance";

const chartTypes = ["Line", "Bar", "Radar"];

export default function Dashboard() {
  const username = "Kutluay";

  const [dbConfig, setDbConfig] = useState({
    host: "",
    port: "",
    database: "",
    username: "",
    password: "",
  });

  const [dataObjects, setDataObjects] = useState([]);
  const [selectedDataObject, setSelectedDataObject] = useState("");
  const [selectedChartType, setSelectedChartType] = useState(chartTypes[0]);
  const [mapping, setMapping] = useState({ xAxis: "", yAxis: "" });
  const [chartData, setChartData] = useState({
    labels: ["Ocak", "Şubat", "Mart", "Nisan"],
    datasets: [
      {
        label: "Örnek Veri",
        data: [12, 19, 7, 15],
        backgroundColor: colorThemes["red"].backgroundColor,
        borderColor: colorThemes["red"].borderColor,
        borderWidth: 2,
        borderDash: [],
        fill: false,
      },
    ],
  });
  const [chartInstance, setChartInstance] = useState(null);

  const [styleConfig, setStyleConfig] = useState({
    colorTheme: "red",
    lineWidth: 2,
    lineStyle: "solid",
  });

  const [axisConfig, setAxisConfig] = useState({
    xAxisLabel: "XLabel",
    yAxisLabel: "YLabel",
    showGrid: true,
  });

const onTestConnection = async (config) => {
  try {
    const response = await axiosInstance.post("/chart/listObjects", {
      host: config.host,
      port: Number(config.port),
      database: config.database,
      username: config.username,
      password: config.password,
    });

    const objectList = response.data.data; // 
    setDataObjects(objectList); // 
    return true;
  } catch (err) {
    console.error("Connection Test Error:", err);
    setDataObjects([]);
    return false;
  }
};


  const showSuccessToast = () => {
    toast.success("Grafik başarıyla oluşturuldu!", {
      position: "top-right",
      autoClose: 2000,
      theme: "colored",
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    

    if (!selectedDataObject || !mapping.xAxis || !mapping.yAxis) {
      toast.error("Lütfen tüm alanları doldurun.");
      return;
    }


    const payload = {
      connection: {
        host: dbConfig.host,
        port: Number(dbConfig.port),
        database: dbConfig.database,
        username: dbConfig.username,
        password: dbConfig.password,
      },
      query: {
        objectType: "TABLE",
        objectName: selectedDataObject,
      },
      mapping: {
        xAxis: mapping.xAxis,
        yAxis: mapping.yAxis,
      },
    };

    try {
      const response = await axiosInstance.post("/chart/fetch", payload);
      const chartApiData = response.data.data;

      const labelsIndex = chartApiData.labels.indexOf(mapping.xAxis);
      const dataIndex = chartApiData.labels.indexOf(mapping.yAxis);

      if (labelsIndex === -1 || dataIndex === -1) {
        toast.error("Mapping error: xAxis or yAxis field not found in data.");
        return;
      }

      const labels = chartApiData.data.map((row) => row[labelsIndex]);
      const dataValues = chartApiData.data.map((row) => Number(row[dataIndex]));

        console.log("chartApiData:", chartApiData);
        console.log("labelsIndex:", labelsIndex, "dataIndex:", dataIndex);
        console.log("labels:", labels);
        console.log("dataValues:", dataValues);


      const themeColors = colorThemes[styleConfig.colorTheme];

      const formattedData = {
        labels: labels,
        datasets: [
          {
            label: selectedDataObject,
            data: dataValues,
            backgroundColor: themeColors.backgroundColor,
            borderColor: themeColors.borderColor,
            borderWidth: styleConfig.lineWidth,
            borderDash:
              styleConfig.lineStyle === "dashed"
                ? [10, 5]
                : styleConfig.lineStyle === "dotted"
                ? [2, 6]
                : [],
            fill: false,
          },
        ],
      };

      setChartData(formattedData);
      showSuccessToast();
    } catch (error) {
      console.error("Chart fetch error:", error);
      toast.error("Sunucudan grafik verisi alınırken hata oluştu.");
    }
  };

  const handleDownload = () => {
    if (chartInstance) {
      const base64 = chartInstance.toBase64Image();
      const link = document.createElement("a");
      link.href = base64;
      link.download = "chart.png";
      link.click();
    } else {
      toast.error("Grafik bulunamadı!");
    }
  };

  return (
    <>
      <Navbar username={username} />
      <div className="min-h-screen bg-white p-6 text-gray-900 mt-16">
        <div className="max-w-8xl mx-auto flex flex-col lg:flex-row gap-12">
          <form
            onSubmit={handleSubmit}
            className="lg:w-1/2 bg-gray-50 p-3 space-y-4 overflow-auto max-h-[200vh]"
          >
            <UnifiedForm
              dbConfig={dbConfig}
              setDbConfig={setDbConfig}
              dataObjects={dataObjects}
              selectedDataObject={selectedDataObject}
              setSelectedDataObject={setSelectedDataObject}
              chartTypes={chartTypes}
              selectedChartType={selectedChartType}
              setSelectedChartType={setSelectedChartType}
              mapping={mapping}
              setMapping={setMapping}
              onTestConnection={onTestConnection}
            />

            <button
              type="submit"
              className="w-full bg-default hover:bg-black border hover:bg-red-500 text-white py-3 rounded-lg font-semibold transition"
            >
              Grafiği Oluştur
            </button>
          </form>

          <div className="lg:w-1/2 bg-white p-8 rounded-lg flex flex-col">
            <ChartStyleSettings
              styleConfig={styleConfig}
              setStyleConfig={setStyleConfig}
              axisConfig={axisConfig}
              setAxisConfig={setAxisConfig}
              chartTypes={chartTypes}
              selectedChartType={selectedChartType}
              setSelectedChartType={setSelectedChartType}
            />

            <div className="flex justify-center items-center min-h-[400px] w-full">
              <ChartDisplay
                chartType={selectedChartType}
                data={chartData}
                styleConfig={{
                  ...styleConfig,
                  colorTheme: colorThemes[styleConfig.colorTheme],
                }}
                axisConfig={axisConfig}
                onChartRef={(ref) => setChartInstance(ref)}
              />
            </div>

            <button
              onClick={handleDownload}
              className="w-full bg-default hover:bg-black text-white font-semibold py-3 px-4 rounded-lg transition"
            >
              Grafiği İndir (.PNG)
            </button>
          </div>
        </div>
      </div>

      <ToastContainer />
    </>
  );
}
