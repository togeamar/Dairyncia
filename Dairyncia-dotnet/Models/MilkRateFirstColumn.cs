using System.ComponentModel.DataAnnotations.Schema;

namespace Dairyncia.Models
{
    public class MilkRateFirstColumn
    {
        public int Id { get; set; }

        public int MilkRateChartId { get; set; }
        public MilkRateChart MilkRateChart { get; set; }

        [Column(TypeName = "decimal(10,2)")]
        public decimal Fat { get; set; }     // 5.5, 5.6, 5.7

        [Column(TypeName = "decimal(10,2)")]
        public decimal Rate { get; set; }    // rate at BaseSnf
    }
}
