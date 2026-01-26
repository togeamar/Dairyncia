using System;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Dairyncia.Migrations
{
    /// <inheritdoc />
    public partial class AddPaymentSettlement : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<int>(
                name: "PaymentSettlementId",
                table: "MilkCollections",
                type: "int",
                nullable: true);

            migrationBuilder.CreateTable(
                name: "PaymentSettlements",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("MySql:ValueGenerationStrategy", MySqlValueGenerationStrategy.IdentityColumn),
                    FarmerId = table.Column<int>(type: "int", nullable: false),
                    ManagerId = table.Column<string>(type: "varchar(255)", nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    FromDate = table.Column<DateTime>(type: "datetime(6)", nullable: false),
                    ToDate = table.Column<DateTime>(type: "datetime(6)", nullable: false),
                    TotalAmount = table.Column<decimal>(type: "decimal(10,2)", nullable: false),
                    PaidAmount = table.Column<decimal>(type: "decimal(10,2)", nullable: false),
                    PendingAmount = table.Column<decimal>(type: "decimal(10,2)", nullable: false),
                    Status = table.Column<int>(type: "int", nullable: false),
                    CreatedAt = table.Column<DateTime>(type: "datetime(6)", nullable: false),
                    UpdatedAt = table.Column<DateTime>(type: "datetime(6)", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_PaymentSettlements", x => x.Id);
                    table.ForeignKey(
                        name: "FK_PaymentSettlements_AspNetUsers_ManagerId",
                        column: x => x.ManagerId,
                        principalTable: "AspNetUsers",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_PaymentSettlements_Farmers_FarmerId",
                        column: x => x.FarmerId,
                        principalTable: "Farmers",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                })
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateIndex(
                name: "IX_MilkCollections_PaymentSettlementId",
                table: "MilkCollections",
                column: "PaymentSettlementId");

            migrationBuilder.CreateIndex(
                name: "IX_PaymentSettlements_FarmerId",
                table: "PaymentSettlements",
                column: "FarmerId");

            migrationBuilder.CreateIndex(
                name: "IX_PaymentSettlements_ManagerId",
                table: "PaymentSettlements",
                column: "ManagerId");

            migrationBuilder.AddForeignKey(
                name: "FK_MilkCollections_PaymentSettlements_PaymentSettlementId",
                table: "MilkCollections",
                column: "PaymentSettlementId",
                principalTable: "PaymentSettlements",
                principalColumn: "Id");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_MilkCollections_PaymentSettlements_PaymentSettlementId",
                table: "MilkCollections");

            migrationBuilder.DropTable(
                name: "PaymentSettlements");

            migrationBuilder.DropIndex(
                name: "IX_MilkCollections_PaymentSettlementId",
                table: "MilkCollections");

            migrationBuilder.DropColumn(
                name: "PaymentSettlementId",
                table: "MilkCollections");
        }
    }
}
