import { useEffect, useState } from "react";
import client from "../../../services/client";

export default function MilkCollectionList() {
  const [records, setRecords] = useState([]);
  const [loading, setLoading] = useState(true);

  const [showModal, setShowModal] = useState(false);
  const [editData, setEditData] = useState(null);

  const formatDate = (dateStr) => {
    return new Date(dateStr).toLocaleDateString("en-GB"); 
  };

  // Fetch records
  const fetchRecords = async () => {
    try {
      const res = await client.get("/admin"); // milk GET added in AdminController
      setRecords(res.data);
    } catch (err) {
      console.error(err);
      alert("Failed to load milk records");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRecords();
  }, []);

  // DELETE
  const handleDelete = async (id) => {
    if (!window.confirm("Delete this milk record?")) return;

    try {
      await client.delete(`/admin/${id}`);
      fetchRecords();
    } catch {
      alert("Delete failed");
    }
  };

  // EDIT
  const openEdit = (record) => {
    setEditData({ ...record });
    setShowModal(true);
  };

  const handleUpdate = async () => {
    try {
      await client.put(`/admin/${editData.id}`, {
        quantity: editData.quantity,
        fatPercentage: editData.fatPercentage,
        snf: editData.snf,
        ratePerLiter: editData.ratePerLiter
      });

      setShowModal(false);
      fetchRecords();
    } catch {
      alert("Update failed");
    }
  };

  if (loading) return <p className="mt-4">Loading...</p>;

  // ENUM MAPS
  const milkTypeMap = {
    1: "Cow",
    2: "Buffalo"
  };

  const milkShiftMap = {
    1: "Morning",
    2: "Evening"
  };

  return (
    <div className="card shadow-sm rounded-4">
      <div className="card-body p-0">
        <table className="table table-bordered table-striped mb-0">
          <thead className="table-dark">
            <tr>
              <th>Farmer</th>
              <th>Manager</th>
              <th>Milk Type</th>
              <th>Shift</th>
              <th>Quantity</th>
              <th>Total Amount</th>
              <th>Date</th>
              <th width="160">Actions</th>
            </tr>
          </thead>

          <tbody>
            {records.length === 0 ? (
              <tr>
                <td colSpan="7" className="text-center">
                  No records found
                </td>
              </tr>
            ) : (
              records.map((r) => (
                <tr key={r.id}>
                  <td>{r.farmerName}</td>
                  <td>{r.managerName}</td>
                  <td>{milkTypeMap[r.milkType]}</td>
                  <td>{milkShiftMap[r.milkShift]}</td>
                  <td>{r.quantity}</td>
                  <td>₹ {r.totalAmount}</td>
                  <td>{formatDate(r.createdAt)}</td>
                  <td>
                    <button
                      className="btn btn-sm btn-primary me-2"
                      onClick={() => openEdit(r)}
                    >
                      Edit
                    </button>
                    <button
                      className="btn btn-sm btn-danger"
                      onClick={() => handleDelete(r.id)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* EDIT MODAL */}
      {showModal && (
        <div className="modal fade show d-block" style={{ background: "#00000080" }}>
          <div className="modal-dialog">
            <div className="modal-content rounded-4">
              <div className="modal-header">
                <h5 className="modal-title">Edit Milk Collection</h5>
                <button className="btn-close" onClick={() => setShowModal(false)} />
              </div>

              <div className="modal-body">
                <input
                  type="number"
                  className="form-control mb-2"
                  placeholder="Quantity"
                  value={editData.quantity}
                  onChange={(e) =>
                    setEditData({ ...editData, quantity: e.target.value })
                  }
                />

                <input
                  type="number"
                  className="form-control mb-2"
                  placeholder="Fat %"
                  value={editData.fatPercentage}
                  onChange={(e) =>
                    setEditData({ ...editData, fatPercentage: e.target.value })
                  }
                />

                <input
                  type="number"
                  className="form-control mb-2"
                  placeholder="SNF"
                  value={editData.snf}
                  onChange={(e) =>
                    setEditData({ ...editData, snf: e.target.value })
                  }
                />

                <input
                  type="number"
                  className="form-control"
                  placeholder="Rate per Liter"
                  value={editData.ratePerLiter}
                  onChange={(e) =>
                    setEditData({ ...editData, ratePerLiter: e.target.value })
                  }
                />
              </div>

              <div className="modal-footer">
                <button
                  className="btn btn-secondary"
                  onClick={() => setShowModal(false)}
                >
                  Cancel
                </button>
                <button className="btn btn-primary" onClick={handleUpdate}>
                  Save Changes
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
