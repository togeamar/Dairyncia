import { useEffect, useState } from "react";
import client from "../../../services/client";
import { Modal, Button, Form } from "react-bootstrap";

export default function ManagerList() {
  const [managers, setManagers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // edit modal states
  const [showEdit, setShowEdit] = useState(false);
  const [selectedManager, setSelectedManager] = useState(null);
  const [fullName, setFullName] = useState("");
  const [phone, setPhone] = useState("");

  // load managers
  const loadManagers = async () => {
    try {
      const response = await client.get("/admin/managers");
      setManagers(response.data);
    } catch (err) {
      setError("Failed to load managers");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadManagers();
  }, []);

  // open edit modal
  const openEditModal = (manager) => {
    setSelectedManager(manager);
    setFullName(manager.fullName ?? "");
    setPhone(manager.phoneNumber ?? "");
    setShowEdit(true);
  };

  // update manager
  const handleUpdate = async () => {
    try {
      await client.put(`/admin/managers/${selectedManager.id}`, {
        fullName: fullName,
        phoneNumber: phone, // MUST match backend DTO
      });

      setShowEdit(false);
      loadManagers();
      alert("Manager updated successfully");
    } catch (err) {
      alert("Failed to update manager");
    }
  };

  // delete manager
  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this manager?"))
      return;

    try {
      await client.delete(`/admin/managers/${id}`);
      loadManagers();
      alert("Manager deleted successfully");
    } catch (err) {
      alert(err.response?.data || "Delete failed");
    }
  };

  if (loading) return <p style={{ paddingTop: "100px" }}>Loading...</p>;
  if (error)
    return (
      <p style={{ paddingTop: "10px", color: "red" }}>{error}</p>
    );

  return (
    <div style={{ paddingTop: "10px", paddingLeft: "30px", paddingRight: "30px" }}>
      <h2>Manager List</h2>

      <table className="table table-bordered table-striped mt-3">
        <thead className="table-dark">
          <tr>
            <th>Sr. No</th>
            <th>Full Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th width="180">Actions</th>
          </tr>
        </thead>

        <tbody>
          {managers.length === 0 ? (
            <tr>
              <td colSpan="5" className="text-center">
                No managers found
              </td>
            </tr>
          ) : (
            managers.map((manager, index) => (
              <tr key={manager.id}>
                <td>{index + 1}</td>
                <td>{manager.fullName ?? "-"}</td>
                <td>{manager.email}</td>
                <td>{manager.phoneNumber ?? "-"}</td>
                <td>
                  <button
                    className="btn btn-sm btn-primary me-2"
                    onClick={() => openEditModal(manager)}
                  >
                    Edit
                  </button>
                  <button
                    className="btn btn-sm btn-danger"
                    onClick={() => handleDelete(manager.id)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>

      {/* EDIT MODAL */}
      <Modal show={showEdit} onHide={() => setShowEdit(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>Edit Manager</Modal.Title>
        </Modal.Header>

        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Full Name</Form.Label>
              <Form.Control
                type="text"
                value={fullName}
                onChange={(e) => setFullName(e.target.value)}
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Phone Number</Form.Label>
              <Form.Control
                type="text"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
              />
            </Form.Group>
          </Form>
        </Modal.Body>

        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowEdit(false)}>
            Cancel
          </Button>
          <Button variant="primary" onClick={handleUpdate}>
            Save Changes
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}
