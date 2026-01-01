import React, { useEffect, useState } from "react";
import axios from "axios";
import { Manager_BASE_URL, ADMIN_BASE_URL } from "../../../constants/ApiConstants";

const AddMilkModal = ({ onClose, onSuccess }) => {
  const [farmers, setFarmers] = useState([]);

  const [form, setForm] = useState({
    farmerEmail: "",
    milkType: 1,
    milkShift: 1,
    quantity: "",
    fatPercentage: "",
    snf: ""
  });

  const [errors, setErrors] = useState({});

  // 🔹 Load farmers
  useEffect(() => {
    const loadFarmers = async () => {
      try {
        const token = localStorage.getItem("token");
        const res = await axios.get(
          `${ADMIN_BASE_URL}/farmers`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        setFarmers(res.data);
      } catch {
        console.error("Failed to load farmers");
      }
    };
    loadFarmers();
  }, []);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setErrors({ ...errors, [e.target.name]: "" }); // clear error on change
  };

  // 🔹 Custom validation
  const validate = () => {
    const newErrors = {};

    if (!form.farmerEmail)
      newErrors.farmerEmail = "Farmer is required";

    if (!form.quantity || form.quantity <= 0)
      newErrors.quantity = "Quantity must be greater than 0";

    if (!form.fatPercentage || form.fatPercentage < 0 || form.fatPercentage > 15)
      newErrors.fatPercentage = "Fat % must be between 0 and 15";

    if (!form.snf || form.snf < 0 || form.snf > 12)
      newErrors.snf = "SNF % must be between 0 and 12";

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    try {
      const token = localStorage.getItem("token");

      await axios.post(
        `${Manager_BASE_URL}/milk-collection/create`,
        {
          farmerEmail: form.farmerEmail,
          milkType: +form.milkType,
          milkShift: +form.milkShift,
          quantity: +form.quantity,
          fatPercentage: +form.fatPercentage,
          snf: +form.snf
        },
        { headers: { Authorization: `Bearer ${token}` } }
      );

      alert("Milk collection added successfully");
      onClose();
    } catch (err) {
      alert(err.response?.data || "Error");
    }

    onSuccess();
  };

  return (
    <>
      {/* BACKDROP */}
      <div
        onClick={onClose}
        style={{
          position: "fixed",
          inset: 0,
          backgroundColor: "rgba(0,0,0,0.6)",
          zIndex: 999
        }}
      />

      {/* MODAL */}
      <div
        style={{
          position: "fixed",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          background: "#fff",
          width: "80%",
          maxWidth: "900px",
          borderRadius: "8px",
          padding: "20px",
          zIndex: 1000
        }}
      >
        {/* HEADER */}
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h5>Add Milk Collection</h5>
          <button className="btn btn-sm btn-danger" onClick={onClose}>✕</button>
        </div>

        {/* FORM */}
        <form onSubmit={handleSubmit}>
          {/* FARMER */}
          <select
            className={`form-select mb-1 ${errors.farmerEmail ? "is-invalid" : ""}`}
            name="farmerEmail"
            onChange={handleChange}
          >
            <option value="">Select Farmer</option>
            {farmers.map(f => (
              <option key={f.id} value={f.email}>
                {f.fullName}
              </option>
            ))}
          </select>
          <div className="invalid-feedback d-block">{errors.farmerEmail}</div>

          <div className="row mt-2">
            <div className="col">
              <select className="form-select" name="milkType" onChange={handleChange}>
                <option value="1">Cow</option>
                <option value="2">Buffalo</option>
              </select>
            </div>

            <div className="col">
              <select className="form-select" name="milkShift" onChange={handleChange}>
                <option value="1">Morning</option>
                <option value="2">Evening</option>
              </select>
            </div>
          </div>

          <div className="row mt-2">
            <div className="col">
              <input
                className={`form-control ${errors.quantity ? "is-invalid" : ""}`}
                name="quantity"
                placeholder="Quantity (L)"
                onChange={handleChange}
              />
              <div className="invalid-feedback">{errors.quantity}</div>
            </div>

            <div className="col">
              <input
                className={`form-control ${errors.fatPercentage ? "is-invalid" : ""}`}
                name="fatPercentage"
                placeholder="Fat %"
                onChange={handleChange}
              />
              <div className="invalid-feedback">{errors.fatPercentage}</div>
            </div>

            <div className="col">
              <input
                className={`form-control ${errors.snf ? "is-invalid" : ""}`}
                name="snf"
                placeholder="SNF %"
                onChange={handleChange}
              />
              <div className="invalid-feedback">{errors.snf}</div>
            </div>
          </div>

          {/* ACTIONS */}
          <div className="text-end mt-3">
            <button type="button" className="btn btn-secondary me-2" onClick={onClose}>
              Cancel
            </button>
            <button className="btn btn-success">Save</button>
          </div>
        </form>
      </div>
    </>
  );
};

export default AddMilkModal;
