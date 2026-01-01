using System.ComponentModel.DataAnnotations.Schema;

namespace Dairyncia.Models
{
    public class MilkRateFirstRow
    {
        public int Id { get; set; }

        public int MilkRateChartId { get; set; }
        public MilkRateChart MilkRateChart { get; set; }

        [Column(TypeName = "decimal(10,2)")]
        public decimal Snf { get; set; }     // 8.7, 8.8, 8.9

        [Column(TypeName = "decimal(10,2)")]
        public decimal Rate { get; set; }    // rate at BaseFat
    }
}
