
import { Formik, Form, Field, ErrorMessage } from "formik";
import { sendContactMessage } from "../../services/contactService";
/* Contact form with validation using Formik 
Formik is a popular library for handling forms in React applications. It simplifies form management by providing tools for form state management, validation, and submission handling.
it reduces the boilerplate code required to create and manage forms, making it easier to build complex forms with less effort.
builtin validation handling ahe  */

/* Contact component jevha user contact form submit krnar tyaveli handleSubmit function call honar
renders a contact form with fields for name, email, and purpose. It uses Formik for form state management and validation.
The validate function checks for required fields and valid email format, returning error messages if validation fails.
On successful submission, handleSubmit logs the form data, shows a success alert, and resets the form.
*/
function Contact() {
  /* Values means object containing form data*/
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
/*errors object madhe je key ahet tyanchya values form madhe display honar */
    return errors;
  };

  /* here request should go to backend ,form is valid then user clicks submit formik calls handle submit */
 const handleSubmit = async (values, { resetForm }) => {
  try {
    const response = await sendContactMessage(values);

    alert(response.data.message);
    resetForm();
  } catch (error) {
    console.error(error);
    alert("Error while sending message");
  }
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
