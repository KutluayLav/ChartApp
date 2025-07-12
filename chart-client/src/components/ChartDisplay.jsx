import { useRef, useMemo } from "react";
import { Line, Bar, Radar } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  RadarController,
  RadialLinearScale,
  Tooltip,
  Legend,
} from "chart.js";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  RadarController,
  RadialLinearScale,
  Tooltip,
  Legend
);

export default function ChartDisplay({
  chartType,
  data,
  styleConfig,
  axisConfig,
  onChartRef,
}) {
  const chartRef = useRef(null);

  const styledData = useMemo(() => {
    if (!data) return null;
    return {
      ...data,
      datasets: data.datasets.map((ds) => ({
        ...ds,
        borderWidth: styleConfig?.lineWidth ?? 2,
        borderDash:
          styleConfig?.lineStyle === "dashed"
            ? [10, 5]
            : styleConfig?.lineStyle === "dotted"
            ? [2, 6]
            : [],
        backgroundColor:
          styleConfig?.colorTheme?.backgroundColor ?? ds.backgroundColor,
        borderColor:
          styleConfig?.colorTheme?.borderColor ?? ds.borderColor,
      })),
    };
  }, [data, styleConfig]);

  const options = useMemo(() => ({
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      x: {
        title: {
          display: !!axisConfig?.xAxisLabel,
          text: axisConfig?.xAxisLabel || "",
        },
        grid: {
          display: axisConfig?.showGrid ?? true,
        },
        type: axisConfig?.scaleType || "category",
      },
      y: {
        title: {
          display: !!axisConfig?.yAxisLabel,
          text: axisConfig?.yAxisLabel || "",
        },
        grid: {
          display: axisConfig?.showGrid ?? true,
        },
        type: axisConfig?.scaleType || "linear",
      },
    },
    plugins: {
      legend: {
        display: true,
        position: "top",
      },
      tooltip: {
        enabled: true,
      },
    },
  }), [axisConfig]);

  if (!styledData) {
    return <div className="text-gray-500 text-center">Grafik verisi yükleniyor...</div>;
  }

  const chartProps = {
    data: styledData,
    options,
    ref: (el) => {
      chartRef.current = el;
      if (onChartRef) onChartRef(el);
    },
  };

  switch (chartType.toLowerCase()) {
    case "line":
      return <Line {...chartProps} />;
    case "bar":
      return <Bar {...chartProps} />;
    case "radar":
      return <Radar {...chartProps} />;
    default:
      return <div>Geçersiz grafik türü: {chartType}</div>;
  }
}
