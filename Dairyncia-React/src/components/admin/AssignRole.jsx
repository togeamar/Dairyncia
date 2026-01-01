import { useState } from "react";
import api from "../../services/api";

export default function AssignRole() {
  const [email, setEmail] = useState("");
  const [role, setRole] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setError("");

    try {
      const response = await api.post("/admin/assign-role", {
        email: email,
        role: role,
      });

      setMessage(response.data.message);
      setEmail("");
      setRole("");
    } catch (err) {
      setError(
        err.response?.data || "Failed to assign role"
      );
    }
  };

  return (
    <div style={{ paddingTop: "100px" }} className="container">
      <h2>Assign Role</h2>

      <form onSubmit={handleSubmit} className="mt-4" style={{ maxWidth: "400px" }}>
        
        <div className="mb-3">
          <label className="form-label">User Email</label>
          <input
            type="email"
            className="form-control"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Role</label>
          <select
            className="form-select"
            value={role}
            onChange={(e) => setRole(e.target.value)}
            required
          >
            <option value="">-- Select Role --</option>
            <option value="Admin">Admin</option>
            <option value="Manager">Manager</option>
            <option value="Farmer">Farmer</option>
          </select>
        </div>

        <button type="submit" className="btn btn-primary">
          Assign Role
        </button>

        {message && (
          <div className="alert alert-success mt-3">{message}</div>
        )}

        {error && (
          <div className="alert alert-danger mt-3">{error}</div>
        )}
      </form>
    </div>
  );
}
