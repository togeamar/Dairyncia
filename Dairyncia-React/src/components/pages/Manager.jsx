import React, { useState } from "react";
import ManagerDashboard from "../Manager/ManagerDashboard";
import FarmerList from "../admin/FarmerList";
import MilkCollectionList from "../admin/MilkCollectionList";
import AddMilkModal from "../Manager/AddMilkModal";
import TodaysMilkCollectionList from "../Manager/TodaysMilkCollectionList"
import "./Manager.css";

const Manager = () => {
  const [activeTab, setActiveTab] = useState("home");
  const [showModal, setShowModal] = useState(false);
  const [refreshKey, setRefreshKey] = useState(0);

   const handleMilkAdded = () => {
    setShowModal(false);
    setRefreshKey(prev => prev + 1); // trigger refresh
  };

  return (
    <div className="manager-page">

      <ul className=" mt-5 nav nav-tabs">
          <li className="nav-item">
            <button
              className={`nav-link ${activeTab === "home" ? "active" : ""}`}
              onClick={() => setActiveTab("home")}
            >
              Manager Dashboard
            </button>
          </li>

          <li className="nav-item">
            <button
              className={`nav-link ${activeTab === "farmers" ? "active" : ""}`}
              onClick={() => setActiveTab("farmers")}
            >
              Farmers
            </button>
          </li>

          <li className="nav-item">
            <button
              className={`nav-link ${activeTab === "milk" ? "active" : ""}`}
              onClick={() => setActiveTab("milk")}
            >
              Total Milk Collections
            </button>
          </li>

          <li className="nav-item">
            <button
              className={`nav-link ${activeTab === "todaysMilk" ? "active" : ""}`}
              onClick={() => setActiveTab("todaysMilk")}>
              Todays Milk Collection
            </button>
          </li>
        </ul>

      {activeTab === "todaysMilk"&& (
        <button
          style={{ marginTop: 20 }}
          className="btn btn-success"
          onClick={() => setShowModal(true)}
        >
          + Add Milk Collection
        </button>
      )}

      <div className="mt-4">
        {activeTab === "home" && <ManagerDashboard />}
        {activeTab === "farmers" && <FarmerList />}
        {activeTab === "milk" && <MilkCollectionList refreshKey={refreshKey} />}
        {activeTab === "todaysMilk" && <TodaysMilkCollectionList refreshKey={ refreshKey }/>}
      </div>

      {showModal && (
        <AddMilkModal
          onClose={() => setShowModal(false)}
          onSuccess={handleMilkAdded}
        />
      )}
    </div>
  );
};

export default Manager;
