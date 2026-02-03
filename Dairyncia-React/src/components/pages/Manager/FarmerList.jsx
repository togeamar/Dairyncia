import { useEffect, useState } from "react";
import client from "../../../services/client";

export default function FarmerList() {
  const [farmers, setFarmers] = useState([]);
  const [loading, setLoading] = useState(true);

  const [selectedFarmer, setSelectedFarmer] = useState(null);
  const [activeForm, setActiveForm] = useState(""); // profile | address | bank
  const [showModal, setShowModal] = useState(false);

  const [profile, setProfile] = useState({
    fullName: "",
    phoneNumber: "",
  });

  const [address, setAddress] = useState({
    village: "",
    city: "",
    state: "",
    pincode: "",
  });

  const [bank, setBank] = useState({
    bankName: "",
    accountNumber: "",
    accountHolderName: "",
    ifsc: "",
  });

  // ================= LOAD FARMERS =================
  useEffect(() => {
    loadFarmers();
  }, []);

  const loadFarmers = async () => {
    try {
      const managerId = localStorage.getItem("id");
      const res = await client.get(`/manager/get-farmer-list/${managerId}`);
      setFarmers(res.data);
    } catch {
      alert("Failed to load farmers");
    } finally {
      setLoading(false);
    }
  };

  // ================= OPEN MODAL =================
  const openModal = async (farmerId, formType) => {
    try {
      const res = await client.get(`/admin/farmers/${farmerId}`);
      const f = res.data;

      setSelectedFarmer(f);
      setActiveForm(formType);
      setShowModal(true);

      if (formType === "profile") {
        setProfile({
          fullName: f.fullName || "",
          phoneNumber: f.phoneNumber || "",
        });
      }

      if (formType === "address") {
        setAddress(
          f.address || {
            village: "",
            city: "",
            state: "",
            pincode: "",
          }
        );
      }

      if (formType === "bank") {
        setBank(
          f.bankDetails || {
            bankName: "",
            accountNumber: "",
            accountHolderName: "",
            ifsc: "",
          }
        );
      }
    } catch {
      alert("Failed to load farmer details");
    }
  };

  const closeModal = () => {
    setShowModal(false);
    setActiveForm("");
    setSelectedFarmer(null);
  };

  // ================= SAVE PROFILE =================
  const saveProfile = async () => {
    try {
      await client.put(
        `/admin/farmers/${selectedFarmer.farmerId}`,
        profile
      );
      alert("Profile updated successfully");
      closeModal();
      loadFarmers();
    } catch {
      alert("Failed to update profile");
    }
  };

  // ================= SAVE ADDRESS =================
  const saveAddress = async () => {
    try {
      await client.post(
        `/admin/farmers/${selectedFarmer.farmerId}/address`,
        address
      );
      alert("Address saved successfully");
      closeModal();
    } catch {
      alert("Failed to save address");
    }
  };

  // ================= SAVE BANK =================
  const saveBank = async () => {
    try {
      await client.post(
        `/admin/farmers/${selectedFarmer.farmerId}/bank`,
        bank
      );
      alert("Bank details saved successfully");
      closeModal();
    } catch {
      alert("Failed to save bank details");
    }
  };

  // ================= DELETE FARMER =================
  const deleteFarmer = async (farmerId) => {
    if (!window.confirm("Are you sure you want to delete this farmer?")) return;

    try {
      await client.delete(`/admin/farmers/${farmerId}`);
      alert("Farmer deleted successfully");
      loadFarmers();
    } catch (err) {
      alert(
        err?.response?.data ||
          "Farmer cannot be deleted. Records exist."
      );
    }
  };

  if (loading) {
    return <p style={{ paddingTop: "100px" }}>Loading farmers...</p>;
  }

  return (
    <div style={{ paddingTop: "100px", padding: "30px" }}>
      <h2>Farmer Management</h2>

      {/* ================= FARMER TABLE ================= */}
      <table className="table table-bordered table-striped mt-3">
        <thead className="table-dark">
          <tr>
            <th>ID</th>
            <th>Full Name</th>
            <th>Email</th>
            <th>Created</th>
            <th width="350">Actions</th>
          </tr>
        </thead>
        <tbody>
          {farmers.length === 0 ? (
            <tr>
              <td colSpan="5" className="text-center">
                No farmers found
              </td>
            </tr>
          ) : (
            farmers.map((f) => (
              <tr key={f.farmerId}>
                <td>{f.farmerId}</td>
                <td>{f.fullName}</td>
                <td>{f.email}</td>
                <td>{new Date(f.createdAt).toLocaleDateString()}</td>
                <td>
                  <button
                    className="btn btn-sm btn-info me-1"
                    onClick={() => openModal(f.farmerId, "profile")}
                  >
                    Edit Profile
                  </button>

                  <button
                    className="btn btn-sm btn-warning me-1"
                    onClick={() => openModal(f.farmerId, "address")}
                  >
                    Address
                  </button>

                  <button
                    className="btn btn-sm btn-primary me-1"
                    onClick={() => openModal(f.farmerId, "bank")}
                  >
                    Bank
                  </button>

                  <button
                    className="btn btn-sm btn-danger"
                    onClick={() => deleteFarmer(f.farmerId)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>

      {/* ================= MODAL ================= */}
      {showModal && (
        <>
          <div className="modal fade show d-block">
            <div className="modal-dialog">
              <div className="modal-content">

                <div className="modal-header">
                  <h5 className="modal-title">
                    {activeForm === "profile" && "Edit Profile"}
                    {activeForm === "address" && "Edit Address"}
                    {activeForm === "bank" && "Edit Bank Details"}
                  </h5>
                  <button className="btn-close" onClick={closeModal}></button>
                </div>

                <div className="modal-body">

                  {/* PROFILE */}
                  {activeForm === "profile" && (
                    <>
                      <input
                        className="form-control mb-2"
                        placeholder="Full Name"
                        value={profile.fullName}
                        onChange={(e) =>
                          setProfile({ ...profile, fullName: e.target.value })
                        }
                      />
                      <input
                        className="form-control"
                        placeholder="Phone Number"
                        value={profile.phoneNumber}
                        onChange={(e) =>
                          setProfile({
                            ...profile,
                            phoneNumber: e.target.value,
                          })
                        }
                      />
                    </>
                  )}

                  {/* ADDRESS */}
                  {activeForm === "address" && (
                    <>
                      <input
                        className="form-control mb-2"
                        placeholder="Village"
                        value={address.village}
                        onChange={(e) =>
                          setAddress({ ...address, village: e.target.value })
                        }
                      />
                      <input
                        className="form-control mb-2"
                        placeholder="City"
                        value={address.city}
                        onChange={(e) =>
                          setAddress({ ...address, city: e.target.value })
                        }
                      />
                      <input
                        className="form-control mb-2"
                        placeholder="State"
                        value={address.state}
                        onChange={(e) =>
                          setAddress({ ...address, state: e.target.value })
                        }
                      />
                      <input
                        className="form-control"
                        placeholder="Pincode"
                        value={address.pincode}
                        onChange={(e) =>
                          setAddress({ ...address, pincode: e.target.value })
                        }
                      />
                    </>
                  )}

                  {/* BANK */}
                  {activeForm === "bank" && (
                    <>
                      <input
                        className="form-control mb-2"
                        placeholder="Bank Name"
                        value={bank.bankName}
                        onChange={(e) =>
                          setBank({ ...bank, bankName: e.target.value })
                        }
                      />
                      <input
                        className="form-control mb-2"
                        placeholder="Account Number"
                        value={bank.accountNumber}
                        onChange={(e) =>
                          setBank({
                            ...bank,
                            accountNumber: e.target.value,
                          })
                        }
                      />
                      <input
                        className="form-control mb-2"
                        placeholder="Account Holder Name"
                        value={bank.accountHolderName}
                        onChange={(e) =>
                          setBank({
                            ...bank,
                            accountHolderName: e.target.value,
                          })
                        }
                      />
                      <input
                        className="form-control"
                        placeholder="IFSC Code"
                        value={bank.ifsc}
                        onChange={(e) =>
                          setBank({ ...bank, ifsc: e.target.value })
                        }
                      />
                    </>
                  )}
                </div>

                <div className="modal-footer">
                  <button className="btn btn-secondary" onClick={closeModal}>
                    Cancel
                  </button>
                  <button
                    className="btn btn-success"
                    onClick={
                      activeForm === "profile"
                        ? saveProfile
                        : activeForm === "address"
                        ? saveAddress
                        : saveBank
                    }
                  >
                    Save
                  </button>
                </div>

              </div>
            </div>
          </div>
          <div className="modal-backdrop fade show"></div>
        </>
      )}
    </div>
  );
}
