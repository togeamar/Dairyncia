import { useEffect, useState } from "react";
import api from "../../services/api";

export default function PendingUsers() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [assigning, setAssigning] = useState({}); // track role assigning status per user

  // Fetch pending users
  useEffect(() => {
    const loadUsers = async () => {
      try {
        const response = await api.get("/admin/pending-users");
        setUsers(response.data);
      } catch (err) {
        setError("Failed to load pending users");
      } finally {
        setLoading(false);
      }
    };

    loadUsers();
  }, []);

  // Assign role
  const handleAssignRole = async (userId, email, role) => {
    setAssigning((prev) => ({ ...prev, [userId]: true }));
    try {
      const response = await api.post("/admin/assign-role", { email, role });
      alert(response.data.message);

      // Remove user from pending list after successful assignment
      setUsers((prev) => prev.filter((u) => u.userId !== userId));
    } catch (err) {
      alert(err.response?.data || "Failed to assign role");
    } finally {
      setAssigning((prev) => ({ ...prev, [userId]: false }));
    }
  };

  if (loading) return <p style={{ paddingTop: "10px" }}>Loading...</p>;
  if (error) return <p style={{ paddingTop: "10px", color: "red" }}>{error}</p>;

  return (
    <div style={{ paddingTop: "10px", paddingLeft: "30px", paddingRight: "30px" }}>
      <h2>Pending Users</h2>
      <table className="table table-bordered table-striped mt-3">
        <thead className="table-dark">
          <tr>
            <th>Sr. No</th>
            <th>Full Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Assign Role</th>
          </tr>
        </thead>

        <tbody>
          {users.length === 0 ? (
            <tr>
              <td colSpan="5" className="text-center">
                No pending users
              </td>
            </tr>
          ) : (
            users.map((user, index) => (
              <tr key={user.userId}>
                <td>{index + 1}</td>
                <td>{user.fullName ?? "-"}</td>
                <td>{user.email}</td>
                <td>{user.phone ?? "-"}</td>
                <td>
                  <select
                    className="form-select"
                    defaultValue=""
                    onChange={(e) => {
                      if (e.target.value) handleAssignRole(user.userId, user.email, e.target.value);
                    }}
                    disabled={assigning[user.userId]}
                  >
                    <option value="">-- Select Role --</option>
                    <option value="Admin">Admin</option>
                    <option value="Manager">Manager</option>
                    <option value="Farmer">Farmer</option>
                  </select>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}
