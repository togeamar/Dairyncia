using Dairyncia.Enums;
using System.ComponentModel.DataAnnotations.Schema;

namespace Dairyncia.Models
{
    public class MilkRateChart
    {
        public int Id { get; set; }

        public string ChartName { get; set; }   // e.g. "Cow Milk Jan-2025"

        [Column(TypeName = "decimal(10,2)")]
        public decimal BaseFat { get; set; }    // 5.5

        [Column(TypeName = "decimal(10,2)")]
        public decimal BaseSnf { get; set; }    // 8.7

        [Column(TypeName = "decimal(10,2)")]
        public decimal BaseRate { get; set; }   // 49.10

        public MilkType MilkType { get; set; }

        public bool IsActive { get; set; }

        public DateTime CreatedOn { get; set; } = DateTime.Now;

        public ICollection<MilkRateFirstRow> FirstRowSnfRates { get; set; }
        public ICollection<MilkRateFirstColumn> FirstColumnFatRates { get; set; }
        public ICollection<MilkRateCell> RateCells { get; set; }
    }
}
