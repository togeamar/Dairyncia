import React, { useEffect, useState } from "react";
import axios from "axios";
import { Manager_BASE_URL } from "../../../constants/ApiConstants";

const ManagerDashboard = () => {
  const [farmers, setFarmers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axios
      .get(`${Manager_BASE_URL}/get-farmer-milk-collection`)
      .then(res => {
        setFarmers(res.data);
        setLoading(false);
      })
      .catch(err => {
        console.error(err);
        setLoading(false);
      });
  }, []);

  if (loading)
    return (
      <div className="d-flex justify-content-center align-items-center vh-100">
        <div className="spinner-border text-primary" />
      </div>
    );

  // ===== TOTALS =====
  const totalCowMilk = farmers.reduce(
    (s, f) => s + f.cowMorningMilkCount + f.cowEveningMilkCount,
    0
  );

  const totalBuffaloMilk = farmers.reduce(
    (s, f) => s + f.buffaloMorningMilkCount + f.buffaloEveningMilkCount,
    0
  );

  const totalMilk = totalCowMilk + totalBuffaloMilk;

  return (
  <>
    {/* KPI CARDS */}
    <div className="row g-3 mb-4">
      <KpiCard
        title="Total Milk (L)"
        value={totalMilk}
        bg="bg-milk-gradient"
      />
      <KpiCard
        title="Cow Milk (L)"
        value={totalCowMilk}
        bg="bg-cow-gradient"
      />
      <KpiCard
        title="Buffalo Milk (L)"
        value={totalBuffaloMilk}
        bg="bg-buffalo-gradient"
      />
    </div>

    {/* TABLE */}
    <div className="card border-0">
      <div className="card-header bg-light fw-semibold">
        Farmer Milk Collection
      </div>

      <div className="table-responsive">
        <table className="table table-hover mb-0">
          <thead className="table-secondary">
            <tr>
              <th>#</th>
              <th>Farmer</th>
              <th>Cow AM</th>
              <th>Cow PM</th>
              <th>Buffalo AM</th>
              <th>Buffalo PM</th>
              <th>Total (L)</th>
            </tr>
          </thead>
          <tbody>
            {farmers.map((f, i) => {
              const total =
                f.cowMorningMilkCount +
                f.cowEveningMilkCount +
                f.buffaloMorningMilkCount +
                f.buffaloEveningMilkCount;

              return (
                <tr key={f.farmerId}>
                  <td>{i + 1}</td>
                  <td className="fw-medium">{f.farmerName}</td>
                  <td>{f.cowMorningMilkCount}</td>
                  <td>{f.cowEveningMilkCount}</td>
                  <td>{f.buffaloMorningMilkCount}</td>
                  <td>{f.buffaloEveningMilkCount}</td>
                  <td className="fw-bold text-primary">{total}</td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  </>
);
};

// ===== KPI CARD =====
const KpiCard = ({ title, value, bg }) => {
  return (
    <div className="col-md-4">
      <div className={`kpi-card ${bg}`}>
        <h6 className="fw-semibold mb-2">{title}</h6>
        <h2 className="fw-bold mb-0">{value}</h2>
      </div>
    </div>
  );
};

export default ManagerDashboard;
