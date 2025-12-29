using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Dairyncia.Migrations
{
    /// <inheritdoc />
    public partial class eighth : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_DairyCenter_Address_AddressId",
                table: "DairyCenter");

            migrationBuilder.DropForeignKey(
                name: "FK_DairyCenter_AspNetUsers_ManagerId",
                table: "DairyCenter");

            migrationBuilder.DropForeignKey(
                name: "FK_Farmer_Address_AddressId",
                table: "Farmer");

            migrationBuilder.DropForeignKey(
                name: "FK_Farmer_AspNetUsers_UserId",
                table: "Farmer");

            migrationBuilder.DropForeignKey(
                name: "FK_MilkCollection_AspNetUsers_ManagerId",
                table: "MilkCollection");

            migrationBuilder.DropForeignKey(
                name: "FK_MilkCollection_Farmer_FarmerId",
                table: "MilkCollection");

            migrationBuilder.DropPrimaryKey(
                name: "PK_MilkCollection",
                table: "MilkCollection");

            migrationBuilder.DropPrimaryKey(
                name: "PK_Farmer",
                table: "Farmer");

            migrationBuilder.DropPrimaryKey(
                name: "PK_DairyCenter",
                table: "DairyCenter");

            migrationBuilder.RenameTable(
                name: "MilkCollection",
                newName: "MilkCollections");

            migrationBuilder.RenameTable(
                name: "Farmer",
                newName: "Farmers");

            migrationBuilder.RenameTable(
                name: "DairyCenter",
                newName: "DairyCenters");

            migrationBuilder.RenameIndex(
                name: "IX_MilkCollection_ManagerId",
                table: "MilkCollections",
                newName: "IX_MilkCollections_ManagerId");

            migrationBuilder.RenameIndex(
                name: "IX_MilkCollection_FarmerId",
                table: "MilkCollections",
                newName: "IX_MilkCollections_FarmerId");

            migrationBuilder.RenameIndex(
                name: "IX_Farmer_UserId",
                table: "Farmers",
                newName: "IX_Farmers_UserId");

            migrationBuilder.RenameIndex(
                name: "IX_Farmer_AddressId",
                table: "Farmers",
                newName: "IX_Farmers_AddressId");

            migrationBuilder.RenameIndex(
                name: "IX_DairyCenter_ManagerId",
                table: "DairyCenters",
                newName: "IX_DairyCenters_ManagerId");

            migrationBuilder.RenameIndex(
                name: "IX_DairyCenter_AddressId",
                table: "DairyCenters",
                newName: "IX_DairyCenters_AddressId");

            migrationBuilder.AddPrimaryKey(
                name: "PK_MilkCollections",
                table: "MilkCollections",
                column: "Id");

            migrationBuilder.AddPrimaryKey(
                name: "PK_Farmers",
                table: "Farmers",
                column: "Id");

            migrationBuilder.AddPrimaryKey(
                name: "PK_DairyCenters",
                table: "DairyCenters",
                column: "Id");

            migrationBuilder.AddForeignKey(
                name: "FK_DairyCenters_Address_AddressId",
                table: "DairyCenters",
                column: "AddressId",
                principalTable: "Address",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_DairyCenters_AspNetUsers_ManagerId",
                table: "DairyCenters",
                column: "ManagerId",
                principalTable: "AspNetUsers",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_Farmers_Address_AddressId",
                table: "Farmers",
                column: "AddressId",
                principalTable: "Address",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_Farmers_AspNetUsers_UserId",
                table: "Farmers",
                column: "UserId",
                principalTable: "AspNetUsers",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_MilkCollections_AspNetUsers_ManagerId",
                table: "MilkCollections",
                column: "ManagerId",
                principalTable: "AspNetUsers",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_MilkCollections_Farmers_FarmerId",
                table: "MilkCollections",
                column: "FarmerId",
                principalTable: "Farmers",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_DairyCenters_Address_AddressId",
                table: "DairyCenters");

            migrationBuilder.DropForeignKey(
                name: "FK_DairyCenters_AspNetUsers_ManagerId",
                table: "DairyCenters");

            migrationBuilder.DropForeignKey(
                name: "FK_Farmers_Address_AddressId",
                table: "Farmers");

            migrationBuilder.DropForeignKey(
                name: "FK_Farmers_AspNetUsers_UserId",
                table: "Farmers");

            migrationBuilder.DropForeignKey(
                name: "FK_MilkCollections_AspNetUsers_ManagerId",
                table: "MilkCollections");

            migrationBuilder.DropForeignKey(
                name: "FK_MilkCollections_Farmers_FarmerId",
                table: "MilkCollections");

            migrationBuilder.DropPrimaryKey(
                name: "PK_MilkCollections",
                table: "MilkCollections");

            migrationBuilder.DropPrimaryKey(
                name: "PK_Farmers",
                table: "Farmers");

            migrationBuilder.DropPrimaryKey(
                name: "PK_DairyCenters",
                table: "DairyCenters");

            migrationBuilder.RenameTable(
                name: "MilkCollections",
                newName: "MilkCollection");

            migrationBuilder.RenameTable(
                name: "Farmers",
                newName: "Farmer");

            migrationBuilder.RenameTable(
                name: "DairyCenters",
                newName: "DairyCenter");

            migrationBuilder.RenameIndex(
                name: "IX_MilkCollections_ManagerId",
                table: "MilkCollection",
                newName: "IX_MilkCollection_ManagerId");

            migrationBuilder.RenameIndex(
                name: "IX_MilkCollections_FarmerId",
                table: "MilkCollection",
                newName: "IX_MilkCollection_FarmerId");

            migrationBuilder.RenameIndex(
                name: "IX_Farmers_UserId",
                table: "Farmer",
                newName: "IX_Farmer_UserId");

            migrationBuilder.RenameIndex(
                name: "IX_Farmers_AddressId",
                table: "Farmer",
                newName: "IX_Farmer_AddressId");

            migrationBuilder.RenameIndex(
                name: "IX_DairyCenters_ManagerId",
                table: "DairyCenter",
                newName: "IX_DairyCenter_ManagerId");

            migrationBuilder.RenameIndex(
                name: "IX_DairyCenters_AddressId",
                table: "DairyCenter",
                newName: "IX_DairyCenter_AddressId");

            migrationBuilder.AddPrimaryKey(
                name: "PK_MilkCollection",
                table: "MilkCollection",
                column: "Id");

            migrationBuilder.AddPrimaryKey(
                name: "PK_Farmer",
                table: "Farmer",
                column: "Id");

            migrationBuilder.AddPrimaryKey(
                name: "PK_DairyCenter",
                table: "DairyCenter",
                column: "Id");

            migrationBuilder.AddForeignKey(
                name: "FK_DairyCenter_Address_AddressId",
                table: "DairyCenter",
                column: "AddressId",
                principalTable: "Address",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_DairyCenter_AspNetUsers_ManagerId",
                table: "DairyCenter",
                column: "ManagerId",
                principalTable: "AspNetUsers",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_Farmer_Address_AddressId",
                table: "Farmer",
                column: "AddressId",
                principalTable: "Address",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_Farmer_AspNetUsers_UserId",
                table: "Farmer",
                column: "UserId",
                principalTable: "AspNetUsers",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_MilkCollection_AspNetUsers_ManagerId",
                table: "MilkCollection",
                column: "ManagerId",
                principalTable: "AspNetUsers",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_MilkCollection_Farmer_FarmerId",
                table: "MilkCollection",
                column: "FarmerId",
                principalTable: "Farmer",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }
    }
}
