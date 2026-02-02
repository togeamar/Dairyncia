import { useState } from "react";

import PendingUsers from "./admin/PendingUsers";
import FarmerList from "./admin/FarmerList";
import ManagerList from "./admin/ManagerList";
import MilkCollectionList from "./admin/MilkCollectionList";

import "./Admin.css";
import Rates from "./admin/Rates";

export default function Admin() {
  const [activeTab, setActiveTab] = useState("pending");

  return (
    <div className="admin-page">
      {/* <h1 className="mb-4 text-center">Admin Dashboard</h1> */}

      {/* Tabs */}
      <ul className=" mt-5 nav nav-tabs">
        <li className="nav-item">
          <button
            className={`nav-link ${activeTab === "pending" ? "active" : ""}`}
            onClick={() => setActiveTab("pending")}
          >
            Pending Users
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
            className={`nav-link ${activeTab === "managers" ? "active" : ""}`}
            onClick={() => setActiveTab("managers")}
          >
            Managers
          </button>
        </li>

        {/*  NEW TAB */}
        <li className="nav-item">
          <button
            className={`nav-link ${activeTab === "milk" ? "active" : ""}`}
            onClick={() => setActiveTab("milk")}
          >
            Milk Collections
          </button>
        </li>
        <li className="nav-item">
          <button
            className={`nav-link ${activeTab==="Rates"?"active":""}`}
            onClick={()=>setActiveTab("Rates")}
            >
              Rate Chart
            </button>
        </li>
      </ul>

      {/* Tab Content */}
      <div className="tab-content mt-4">
        {activeTab === "pending" && <PendingUsers />}
        {activeTab === "farmers" && <FarmerList />}
        {activeTab === "managers" && <ManagerList />}
        {activeTab === "milk" && <MilkCollectionList />}
        {activeTab === "Rates" && <Rates/>}
      </div>
    </div>
  );
}
