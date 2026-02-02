import React, { useEffect, useState } from "react";
import "./FarmerDashboard.css";

const FarmerDashboard = () => {
  const [dashboardData, setDashboardData] = useState({
    fullName: "Farmer Name",
    email: "email@example.com",
    phone: "N/A",
    bankDetails: null,
    managers: []
  });

  const [loading, setLoading] = useState(true);

  useEffect(() => {
  
    fetch("http://localhost:5225/api/farmer/dashboard/0")
      .then((res) => {
        if (!res.ok) {
          throw new Error("No data yet");
        }
        return res.json();
      })
      .then((data) => {
        setDashboardData({
          fullName: data.fullName || "Farmer Name",
          email: data.email || "email@example.com",
          phone: data.phone || "N/A",
          bankDetails: data.bankDetails || null,
          managers: data.managers || []
        });
        setLoading(false);
      })
      .catch(() => {

        setLoading(false);
      });
  }, []);

  if (loading) {
    return <div className="fd-loading">Loading dashboard...</div>;
  }

  return (
    <div className="fd-container fade-in">
      <h2 className="fd-title">Farmer Dashboard</h2>

      {/* PROFILE + BANK */}
      <div className="fd-top-grid">
        <div className="fd-card slide-up delay-1">
          <h3> Profile</h3>
          <p><strong>Name:</strong> {dashboardData.fullName}</p>
          <p><strong>Email:</strong> {dashboardData.email}</p>
          <p><strong>Phone:</strong> {dashboardData.phone}</p>
        </div>

        <div className="fd-card slide-up delay-2">
          <h3> Bank Details</h3>
          {dashboardData.bankDetails ? (
            <>
              <p><strong>Bank:</strong> {dashboardData.bankDetails.bankName}</p>
              <p><strong>Account No:</strong> {dashboardData.bankDetails.accountNumber}</p>
            </>
          ) : (
            <p className="fd-muted">Bank details not added</p>
          )}
        </div>
      </div>

      {/* MANAGER PAYMENTS */}
      <h3 className="fd-subtitle">Manager-wise Payments</h3>

      {dashboardData.managers.length === 0 ? (
        <p className="fd-empty">No payment data available</p>
      ) : (
        <div className="fd-manager-grid">
          {dashboardData.managers.map((mgr, index) => (
            <div
              key={mgr.managerId || index}
              className={`fd-manager-card slide-up delay-${Math.min(index + 1, 4)}`}
            >
              <h4>{mgr.managerName}</h4>
              <p>Total: ₹ {mgr.totalAmount}</p>
              <p className="paid">Paid: ₹ {mgr.paidAmount}</p>
              <p className="pending">Pending: ₹ {mgr.pendingAmount}</p>

              <span className={`status ${mgr.paymentStatus?.toLowerCase() || "pending"}`}>
                {mgr.paymentStatus || "Pending"}
              </span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default FarmerDashboard;
