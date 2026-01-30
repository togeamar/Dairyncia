import { useEffect, useState } from "react";
import api from "../../../services/api";

export default function PendingUsers() {
  const [users, setUsers] = useState([]);
  const [managers, setManagers] = useState([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [assigning, setAssigning] = useState({}); // loading per user
  const [selectedRoles, setSelectedRoles] = useState({}); // { userId: "Farmer" }
  const [selectedManagers, setSelectedManagers] = useState({}); // { userId: "managerId" }

  const fetchManagers = async () => {
    try {
      const response = await api.get("/admin/managers");
      setManagers(response.data);
    } catch (err) {
      console.log(err);
    }
  };

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

  useEffect(() => {
    fetchManagers();
    loadUsers();
  }, []);

  // Role select (only save role, don’t call API here)
  const handleRoleChange = (userId, role) => {
    setSelectedRoles((prev) => ({ ...prev, [userId]: role }));

    // if role is not Farmer, remove manager selection for that user
    if (role !== "Farmer") {
      setSelectedManagers((prev) => {
        const copy = { ...prev };
        delete copy[userId];
        return copy;
      });
    }
  };

  // Manager select (save managerId per user)
  const handleManagerChange = (userId, managerId) => {
    setSelectedManagers((prev) => ({ ...prev, [userId]: managerId }));
  };

  //Assign role API call
  const handleAssignRole = async (user) => {
    const role = selectedRoles[user.userId];
    const managerId = selectedManagers[user.userId];

    if (!role) {
      alert("Please select role");
      return;
    }

    if (role === "Farmer" && !managerId) {
      alert("Please select manager for Farmer");
      return;
    }

    setAssigning((prev) => ({ ...prev, [user.userId]: true }));

    try {
      const payload = {
        email: user.email,
        role: role,
        managerId: role === "Farmer" ? managerId : "",
      };

      const response = await api.post("/admin/assign-role", payload);
      alert(response.data.message);

      // remove user after success
      setUsers((prev) => prev.filter((u) => u.userId !== user.userId));

      // cleanup selected states
      setSelectedRoles((prev) => {
        const copy = { ...prev };
        delete copy[user.userId];
        return copy;
      });

      setSelectedManagers((prev) => {
        const copy = { ...prev };
        delete copy[user.userId];
        return copy;
      });
    } catch (err) {
      alert(err.response?.data || "Failed to assign role");
    } finally {
      setAssigning((prev) => ({ ...prev, [user.userId]: false }));
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
            <th>Role</th>
            <th>Manager</th>
            <th>Action</th>
          </tr>
        </thead>

        <tbody>
          {users.length === 0 ? (
            <tr>
              <td colSpan="7" className="text-center">
                No pending users
              </td>
            </tr>
          ) : (
            users.map((user, index) => {
              const role = selectedRoles[user.userId] || "";

              return (
                <tr key={user.userId}>
                  <td>{index + 1}</td>
                  <td>{user.fullName ?? "-"}</td>
                  <td>{user.email}</td>
                  <td>{user.phone ?? "-"}</td>

                  {/* Role Dropdown */}
                  <td>
                    <select
                      className="form-select"
                      value={role}
                      onChange={(e) => handleRoleChange(user.userId, e.target.value)}
                      disabled={assigning[user.userId]}
                    >
                      <option value="">-- Select Role --</option>
                      <option value="Admin">Admin</option>
                      <option value="Manager">Manager</option>
                      <option value="Farmer">Farmer</option>
                    </select>
                  </td>

                  {/* Manager Dropdown only for Farmer */}
                  <td>
                    {role === "Farmer" ? (
                      <select
                        className="form-select"
                        value={selectedManagers[user.userId] || ""}
                        onChange={(e) => handleManagerChange(user.userId, e.target.value)}
                        disabled={assigning[user.userId]}
                      >
                        <option value="">-- Select Manager --</option>
                        {managers.map((m) => (
                          <option key={m.managerId} value={m.managerId}>
                            {m.fullName} ({m.email})
                          </option>
                        ))}
                      </select>
                    ) : (
                      <span>-</span>
                    )}
                  </td>

                  {/*Assign Button */}
                  <td>
                    <button
                      className="btn btn-success btn-sm"
                      onClick={() => handleAssignRole(user)}
                      disabled={assigning[user.userId]}
                    >
                      {assigning[user.userId] ? "Assigning..." : "Assign"}
                    </button>
                  </td>
                </tr>
              );
            })
          )}
        </tbody>
      </table>
    </div>
  );
}
