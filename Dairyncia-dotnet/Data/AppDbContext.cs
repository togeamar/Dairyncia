using Dairyncia.Models;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;


public class AppDbContext : IdentityDbContext<ApplicationUser>
{
    public AppDbContext(DbContextOptions<AppDbContext> options)
        : base(options) { }

    public DbSet<Farmer> Farmers { get; set; }
    public DbSet<MilkCollection> MilkCollections { get; set; }
    public DbSet<BankDetails> BankDetails { get; set; }
    public DbSet<DairyCenter> DairyCenters { get; set; }
    public DbSet<ContactMessage> ContactMessages { get; set; }

    public DbSet<MilkRateChart> MilkRateCharts { get; set; }
    public DbSet<MilkRateFirstRow> MilkRateFirstRows { get; set; }
    public DbSet<MilkRateFirstColumn> MilkRateFirstColumns { get; set; }
    public DbSet<MilkRateCell> MilkRateCells { get; set; }

    public DbSet<Address> Addresses { get; set; }
    protected override void OnModelCreating(ModelBuilder builder)
    {
        base.OnModelCreating(builder);

        // ContactMessage constraints
builder.Entity<ContactMessage>(entity =>
{
    entity.Property(e => e.Name)
          .IsRequired();

    entity.Property(e => e.Email)
          .IsRequired();

    entity.Property(e => e.Purpose)
          .IsRequired();

    entity.Property(e => e.CreatedAt)
          .IsRequired();
});

        // Farmer → User
        builder.Entity<Farmer>()
            .HasOne(f => f.User)
            .WithOne()
            .HasForeignKey<Farmer>(f => f.UserId)
            .OnDelete(DeleteBehavior.Restrict);

        // Farmer → Address
        builder.Entity<Farmer>()
            .HasOne(f => f.Address)
            .WithMany()
            .HasForeignKey(f => f.AddressId)
            .OnDelete(DeleteBehavior.Restrict);

        // BankDetails → User
        builder.Entity<BankDetails>()
            .HasOne(b => b.User)
            .WithOne()
            .HasForeignKey<BankDetails>(b => b.UserId)
            .OnDelete(DeleteBehavior.Restrict);

        // MilkCollection → Farmer
        builder.Entity<MilkCollection>()
            .HasOne(mc => mc.Farmer)
            .WithMany(f => f.MilkCollections)
            .HasForeignKey(mc => mc.FarmerId)
            .OnDelete(DeleteBehavior.Restrict);

        // MilkCollection → Manager
        builder.Entity<MilkCollection>()
            .HasOne(mc => mc.Manager)
            .WithMany()
            .HasForeignKey(mc => mc.ManagerId)
            .OnDelete(DeleteBehavior.Restrict);

        // DairyCenter → Address
        builder.Entity<DairyCenter>()
            .HasOne(dc => dc.Address)
            .WithMany()
            .HasForeignKey(dc => dc.AddressId)
            .OnDelete(DeleteBehavior.Restrict);

        // DairyCenter → Manager
        builder.Entity<DairyCenter>()
            .HasOne(dc => dc.Manager)
            .WithMany()
            .HasForeignKey(dc => dc.ManagerId)
            .OnDelete(DeleteBehavior.Restrict);

        builder.Entity<MilkRateCell>()
            .HasIndex(x => new { x.Fat, x.Snf, x.MilkRateChartId })
            .IsUnique();
    }

}

