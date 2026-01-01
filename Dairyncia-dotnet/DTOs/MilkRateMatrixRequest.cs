namespace Dairyncia.DTOs
{
    public class MilkRateMatrixRequest
    {
        // Base reference
        public string ChartName { get; set; }
        public string MilkType { get; set; }
        public decimal BaseFat { get; set; } = 5.5m;
        public decimal BaseSnf { get; set; } = 8.7m;
        public decimal BaseRate { get; set; }   // value at (5.5, 8.7)

        // FIRST ROW: SNF → Rate (Fat fixed at BaseFat)
        public Dictionary<decimal, decimal> FirstRowSnfRates { get; set; }

        // FIRST COLUMN: FAT → Rate (SNF fixed at BaseSnf)
        public Dictionary<decimal, decimal> FirstColumnFatRates { get; set; }
    }
}
