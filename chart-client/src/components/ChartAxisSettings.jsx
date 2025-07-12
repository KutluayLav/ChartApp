export default function ChartAxisSettings({ axisConfig, setAxisConfig }) {
  const handleXAxisLabelChange = (e) => {
    setAxisConfig((prev) => ({ ...prev, xAxisLabel: e.target.value }));
  };

  const handleYAxisLabelChange = (e) => {
    setAxisConfig((prev) => ({ ...prev, yAxisLabel: e.target.value }));
  };

  const handleShowGridChange = (e) => {
    setAxisConfig((prev) => ({ ...prev, showGrid: e.target.checked }));
  };

  return (
    <div className="space-y-2 bg-white p-2">
      <h3 className="font-semibold text-lg mb-2">Grafik Ekseni Ayarları</h3>

      {/* X ve Y Ekseni etiketlerini yan yana göster */}
      <div className="flex gap-1">
        <div className="flex-1 block items-center space-y-2">
          <label className="font-medium ">X Ekseni Etiketi:</label>
          <input
            type="text"
            value={axisConfig.xAxisLabel}
            onChange={handleXAxisLabelChange}
            className="flex-grow p-2 border rounded"
          />
        </div>

        <div className="flex-1 block items-center space-y-2">
          <label className="font-medium ">Y Ekseni Etiketi:</label>
          <input
            type="text"
            value={axisConfig.yAxisLabel}
            onChange={handleYAxisLabelChange}
            className="flex-grow p-2 border rounded"
          />
        </div>
      </div>

      {/* Grid gösterme checkbox */}
      <div className="flex items-center space-x-2">
        <input
          type="checkbox"
          checked={axisConfig.showGrid}
          onChange={handleShowGridChange}
          id="showGridCheckbox"
        />
        <label htmlFor="showGridCheckbox" className="select-none">
          Grid Görünsün
        </label>
      </div>
    </div>
  );
}
