using System;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Dairyncia.Migrations
{
    /// <inheritdoc />
    public partial class RemoveOldMilkRateLogic : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "MilkRateCells");

            migrationBuilder.DropTable(
                name: "MilkRateFirstColumns");

            migrationBuilder.DropTable(
                name: "MilkRateFirstRows");

            migrationBuilder.DropTable(
                name: "MilkRateCharts");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "MilkRateCharts",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("MySql:ValueGenerationStrategy", MySqlValueGenerationStrategy.IdentityColumn),
                    BaseFat = table.Column<decimal>(type: "decimal(10,2)", nullable: false),
                    BaseRate = table.Column<decimal>(type: "decimal(10,2)", nullable: false),
                    BaseSnf = table.Column<decimal>(type: "decimal(10,2)", nullable: false),
                    ChartName = table.Column<string>(type: "longtext", nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    CreatedOn = table.Column<DateTime>(type: "datetime(6)", nullable: false),
                    IsActive = table.Column<bool>(type: "tinyint(1)", nullable: false),
                    MilkType = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_MilkRateCharts", x => x.Id);
                })
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateTable(
                name: "MilkRateCells",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("MySql:ValueGenerationStrategy", MySqlValueGenerationStrategy.IdentityColumn),
                    MilkRateChartId = table.Column<int>(type: "int", nullable: false),
                    Fat = table.Column<decimal>(type: "decimal(10,2)", nullable: false),
                    Rate = table.Column<decimal>(type: "decimal(10,2)", nullable: false),
                    Snf = table.Column<decimal>(type: "decimal(10,2)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_MilkRateCells", x => x.Id);
                    table.ForeignKey(
                        name: "FK_MilkRateCells_MilkRateCharts_MilkRateChartId",
                        column: x => x.MilkRateChartId,
                        principalTable: "MilkRateCharts",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                })
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateTable(
                name: "MilkRateFirstColumns",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("MySql:ValueGenerationStrategy", MySqlValueGenerationStrategy.IdentityColumn),
                    MilkRateChartId = table.Column<int>(type: "int", nullable: false),
                    Fat = table.Column<decimal>(type: "decimal(10,2)", nullable: false),
                    Rate = table.Column<decimal>(type: "decimal(10,2)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_MilkRateFirstColumns", x => x.Id);
                    table.ForeignKey(
                        name: "FK_MilkRateFirstColumns_MilkRateCharts_MilkRateChartId",
                        column: x => x.MilkRateChartId,
                        principalTable: "MilkRateCharts",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                })
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateTable(
                name: "MilkRateFirstRows",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("MySql:ValueGenerationStrategy", MySqlValueGenerationStrategy.IdentityColumn),
                    MilkRateChartId = table.Column<int>(type: "int", nullable: false),
                    Rate = table.Column<decimal>(type: "decimal(10,2)", nullable: false),
                    Snf = table.Column<decimal>(type: "decimal(10,2)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_MilkRateFirstRows", x => x.Id);
                    table.ForeignKey(
                        name: "FK_MilkRateFirstRows_MilkRateCharts_MilkRateChartId",
                        column: x => x.MilkRateChartId,
                        principalTable: "MilkRateCharts",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                })
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateIndex(
                name: "IX_MilkRateCells_Fat_Snf_MilkRateChartId",
                table: "MilkRateCells",
                columns: new[] { "Fat", "Snf", "MilkRateChartId" },
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_MilkRateCells_MilkRateChartId",
                table: "MilkRateCells",
                column: "MilkRateChartId");

            migrationBuilder.CreateIndex(
                name: "IX_MilkRateFirstColumns_MilkRateChartId",
                table: "MilkRateFirstColumns",
                column: "MilkRateChartId");

            migrationBuilder.CreateIndex(
                name: "IX_MilkRateFirstRows_MilkRateChartId",
                table: "MilkRateFirstRows",
                column: "MilkRateChartId");
        }
    }
}
