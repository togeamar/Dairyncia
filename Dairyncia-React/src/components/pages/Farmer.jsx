import { useEffect, useState } from "react";
import api from "../../services/api";
import "./Farmer.css";

export default function Farmer() {
  const [profile, setProfile] = useState({});
  const [records, setRecords] = useState([]);
  const [summary, setSummary] = useState({
    totalIncome: 0,
    paidAmount: 0,
    unpaidAmount: 0
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) return;

    const fetchData = async () => {
      try {
        // Fetch profile
        const profileRes = await api.get("/farmer/profile", {
          headers: { Authorization: `Bearer ${token}` }
        });
        setProfile(profileRes.data);

        // Fetch milk collections
        const recordsRes = await api.get("/farmer/milk-collections", {
          headers: { Authorization: `Bearer ${token}` }
        });

        const data = recordsRes.data || [];
        setRecords(data);

        // ---------- INCOME CALCULATION ----------
        const totalIncome = data
          .filter(r => r.paymentStatus !== "Cancelled")
          .reduce((sum, r) => sum + r.totalAmount, 0);

        const paidAmount = data
          .filter(r => r.paymentStatus === "Paid")
          .reduce((sum, r) => sum + r.totalAmount, 0);

        const unpaidAmount = data
          .filter(r => r.paymentStatus === "Pending")
          .reduce((sum, r) => sum + r.totalAmount, 0);

        setSummary({ totalIncome, paidAmount, unpaidAmount });

      } catch (err) {
        console.error("Farmer Dashboard API error:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) {
    return (
      <div style={{ textAlign: "center", marginTop: "100px" }}>
        <h3>Loading Farmer Dashboard...</h3>
      </div>
    );
  }

  return (
    <div className="farmer-page">

      {/* PROFILE + ADDRESS */}
<div className="profile-address-wrapper">

  {/* LEFT : PERSONAL DETAILS */}
  <div className="profile-card">
    <div className="profile-left">
      <img
        src="https://cdn-icons-png.flaticon.com/512/219/219983.png"
        alt="Farmer"
      />
    </div>
    <div className="profile-right">
      <h1>{profile.fullName}</h1>
      <h5><strong>Email:</strong> {profile.email}</h5>
      <h5><strong>Phone:</strong> {profile.phoneNumber || "N/A"}</h5>
    </div>
  </div>

  {/* RIGHT : ADDRESS */}
  <div className="address-card">
    <h3>🏠 Address</h3>
    <p><strong>Village:</strong> {profile.village}</p>
    <p><strong>City:</strong> {profile.city}</p>
    <p><strong>State:</strong> {profile.state}</p>
    <p><strong>Pincode:</strong> {profile.pincode}</p>
  </div>

</div>


      {/* INCOME SUMMARY */}
      <div className="income-summary">
        <h3>💰 Income Summary (This Month)</h3>

        <table className="summary-table">
          <tbody>
            <tr>
              <td><strong>Total Income</strong></td>
              <td>₹ {summary.totalIncome.toFixed(2)}</td>
            </tr>
            <tr>
              <td><strong>Paid Amount</strong></td>
              <td style={{ color: "green" }}>
                ₹ {summary.paidAmount.toFixed(2)}
              </td>
            </tr>
            <tr>
              <td><strong>Unpaid Amount</strong></td>
              <td style={{ color: "red" }}>
                ₹ {summary.unpaidAmount.toFixed(2)}
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      {/* MILK COLLECTIONS TABLE */}
      <div className="records-section">
        <h3>🥛 Previous Milk Collections</h3>
        <table className="records-table">
          <thead>
            <tr>
              <th>Date</th>
              <th>Milk Type</th>
              <th>Shift</th>
              <th>Quantity (L)</th>
              <th>Fat (%)</th>
              <th>SNF</th>
              <th>Rate/L (₹)</th>
              <th>Total (₹)</th>
            </tr>
          </thead>
          <tbody>
            {records.length === 0 ? (
              <tr>
                <td colSpan="8" className="no-data">
                  No milk records found
                </td>
              </tr>
            ) : (
              records.map((r) => (
                <tr key={r.id}>
                  <td>{new Date(r.createdAt).toLocaleDateString()}</td>
                  <td>{r.milkType}</td>
                  <td>{r.milkShift}</td>
                  <td>{r.quantity}</td>
                  <td>{r.fatPercentage}</td>
                  <td>{r.snf}</td>
                  <td>{r.ratePerLiter}</td>
                  <td>{r.totalAmount}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

    </div>
  );
}
