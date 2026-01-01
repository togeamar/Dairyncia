import { Formik, Form, Field, ErrorMessage } from "formik";

function Contact() {
  const validate = (values) => {
    const errors = {};

    if (!values.name) {
      errors.name = "Name is required";
    }

    if (!values.email) {
      errors.email = "Email is required";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(values.email)) {
      errors.email = "Invalid email address";
    }

    if (!values.purpose) {
      errors.purpose = "Please tell us why you are contacting";
    }

    return errors;
  };

  const handleSubmit = (values, { resetForm }) => {
    console.log("Contact Data:", values);
    alert("Message submitted successfully");
    resetForm();
  };

  return (
    <div className="container my-0">
      <h2 className="text-center mb-4">Contact Us</h2>

      <Formik
        initialValues={{ name: "", email: "", purpose: "" }}
        validate={validate}
        onSubmit={handleSubmit}
      >
        <Form className="col-md-6 mx-auto">
          <div className="mb-3">
            <label className="form-label">Name</label>
            <Field name="name" className="form-control" />
            <div className="text-danger small">
              <ErrorMessage name="name" />
            </div>
          </div>

          <div className="mb-3">
            <label className="form-label">Email</label>
            <Field name="email" className="form-control" />
            <div className="text-danger small">
              <ErrorMessage name="email" />
            </div>
          </div>

          <div className="mb-3">
            <label className="form-label">Purpose</label>
            <Field
              as="textarea"
              name="purpose"
              rows="4"
              className="form-control"
            />
            <div className="text-danger small">
              <ErrorMessage name="purpose" />
            </div>
          </div>

          <button type="submit" className="btn btn-primary w-100">
            Submit
          </button>
        </Form>
      </Formik>
    </div>
  );
}

export default Contact;
