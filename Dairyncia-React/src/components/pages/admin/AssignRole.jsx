import { useEffect, useState } from "react";
import api from "../../../services/api";

export default function AssignRole() {
  const [email, setEmail] = useState("");
  const [role, setRole] = useState("");
  const [managerId, setManagerId] = useState("");
  const [managers, setManagers] = useState([]);

  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  // get all managers
  useEffect(() => {
    const fetchManagers = async () => {
      try {
        const response = await api.get("/admin/managers");
        setManagers(response.data); // expected array
      } catch (err) {
        console.log(err);
      }
    };

    fetchManagers();
  }, []);

  //role change
  const handleRoleChange = (e) => {
    const selectedRole = e.target.value;
    setRole(selectedRole);

    // if not Farmer, clear manager selection
    if (selectedRole !== "Farmer") {
      setManagerId("");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setError("");

    //if Farmer selected, manager is required
    if (role === "Farmer" && !managerId) {
      setError("Please select manager for Farmer");
      return;
    }

    try {
      const payload = {
        email,
        role,
        managerId: role === "Farmer" ? managerId : "",
      };

      const response = await api.post("/admin/assign-role", payload);

      setMessage(response.data.message);
      setEmail("");
      setRole("");
      setManagerId("");
    } catch (err) {
      setError(err.response?.data || "Failed to assign role");
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
            onChange={handleRoleChange}
            required
          >
            <option value="">-- Select Role --</option>
            <option value="Admin">Admin</option>
            <option value="Manager">Manager</option>
            <option value="Farmer">Farmer</option>
          </select>
        </div>

        {/*Show only when Farmer selected */}
        {role === "Farmer" && (
          <div className="mb-3">
            <label className="form-label">Assign Manager</label>
            <select
              className="form-select"
              value={managerId}
              onChange={(e) => setManagerId(e.target.value)}
              required
            >
              <option value="">-- Select Manager --</option>

              {managers.map((m) => (
                <option key={m.id} value={m.id}>
                  {m.fullName} ({m.email})
                </option>
              ))}
            </select>
          </div>
        )}

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