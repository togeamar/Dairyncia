using System.ComponentModel.DataAnnotations.Schema;

namespace Dairyncia.Models
{
    public class MilkRateCell
    {
        public int Id { get; set; }

        public int MilkRateChartId { get; set; }
        public MilkRateChart MilkRateChart { get; set; }

        [Column(TypeName = "decimal(10,2)")]
        public decimal Fat { get; set; }

        [Column(TypeName = "decimal(10,2)")]
        public decimal Snf { get; set; }

        [Column(TypeName = "decimal(10,2)")]
        public decimal Rate { get; set; }
    }
}
