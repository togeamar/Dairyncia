import React, { useState } from "react";
import { Container, Row, Col, Card, Button, Spinner, Alert } from "react-bootstrap";
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import { motion } from "framer-motion";
import { useNavigate } from "react-router-dom";
import "./SignUp.css";
import { doSignup } from "../../services/userservices";
import { Eye, EyeSlash } from "react-bootstrap-icons";

export function Signup() {
  const navigate = useNavigate();
  const [serverError, setServerError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);


  const SignUpSchema = Yup.object().shape({
    fullName: Yup.string().min(2, "Too short!").required("First name is required"),
    email: Yup.string().email("Invalid email").required("Email is required"),
    password: Yup.string()
      .min(6, "Password must be at least 6 characters")
      .required("Password is required")
      .max(16, "Password must not exceed 16 characters")
      .matches(/[a-z]/, "Password must contain at least one lowercase letter")
      .matches(/[A-Z]/, "Password must contain at least one uppercase letter")
      .matches(/[0-9]/, "Password must contain at least one phone")
      .matches(/[^A-Za-z0-9]/, "Password must contain at least one special character"),
      confirmPassword: Yup.string().oneOf([Yup.ref("password"),null],"passwords must match").required("confirm password is required"),
    phone:Yup.string().matches(/^[6-9]\d{9}$/, "Enter a valid 10-digit phone phone").required("Phone phone is required"),
  });

  const togglepassword = () => setShowPassword(!showPassword)

  const handleSubmit = async (values, { resetForm }) => {
    console.log(values);
    setLoading(true);
    setServerError(null);
    try {
      const res = await doSignup(values);
      setSuccess("Account created successfully!");
      resetForm();
      setTimeout(() => navigate("/"), 1500);
    } catch (error) {
      setServerError(error.response?.data[0].description|| "Signup failed!");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container fluid className="signupcontainer d-flex-row justify-content-center align-items-start">
      
      <Row className="w-100 mx-2 justify-content-end">
        <Col className="card" sm={8} md={6} lg={5}>
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
          >
            <Card className="shadow-lg mt-5 border-0 rounded-4 p-4 signup-card">
              <Card.Title className="signup-title text-center mb-3 fw-bold fs-3 text-primary">
                User Signup
              </Card.Title>

              {serverError && (
                <Alert
                  variant="danger"
                  dismissible
                  onClose={() => setServerError(null)}
                >
                  {serverError}
                </Alert>
              )}

              {success && (
                <Alert
                  variant="success"
                  dismissible
                  onClose={() => setSuccess(null)}
                >
                  {success}
                </Alert>
              )}

              <Formik
                initialValues={{fullName: "",confirmPassword: "",email: "",phone:"",password: "",
                }}
                validationSchema={SignUpSchema}
                onSubmit={handleSubmit}
              >
                {({ touched, errors }) => (
                  <Form>
                    <div className="mb-2">
                      <label className="form-label fw-semibold">Full Name</label>
                      <Field
                        name="fullName"
                        type="text"
                        className={`form-control ${touched.fullName && errors.fullName ? "is-invalid" : ""
                          }`}
                        placeholder="Enter first name"
                      />
                      <ErrorMessage
                        component="div"
                        name="fullName"
                        className="invalid-feedback"
                      />
                    </div>

                    

                    <div className="mb-2">
                      <label className="form-label fw-semibold">Email</label>
                      <Field
                        name="email"
                        type="email"
                        className={`form-control ${touched.email && errors.email ? "is-invalid" : ""
                          }`}
                        placeholder="Enter email"
                      />
                      <ErrorMessage
                        component="div"
                        name="email"
                        className="invalid-feedback"
                      />
                    </div>
                    <div className="mb-2">
                      <label className="form-label fw-semibold">phone</label>
                      <Field
                        name="phone"
                        type="text"
                        className={`form-control ${touched.phone && errors.phone ? "is-invalid" : ""
                          }`}
                        placeholder="Enter phone"
                      />
                      <ErrorMessage
                        component="div"
                        name="phone"
                        className="invalid-feedback"
                      />
                    </div>
                    


                    <div className="mb-2">
                      <label className="form-label fw-semibold">Password</label>
                      <Field
                        name="password"
                        type={showPassword ? "text" : "password"}
                        className={`form-control ${touched.password && errors.password ? "is-invalid" : ""
                          }`}
                        placeholder="Enter password"
                      />
                      <ErrorMessage
                        component="div"
                        name="password"
                        className="invalid-feedback"
                      />
                      
                    </div>
                    <div className="mb-2">
                      <label className="form-label fw-semibold">Confirm Password</label>
                      <Field
                        name="confirmPassword"
                        type={showPassword?"text":"password"}
                        className={`form-control ${touched.confirmPassword && errors.confirmPassword ? "is-invalid" : ""
                          }`}
                        placeholder="Enter password again!"
                      />
                      <ErrorMessage
                        component="div"
                        name="confirmPassword"
                        className="invalid-feedback"
                      />
                      <Button variant="outline-secondary" onClick={togglepassword}>
                        {showPassword ? <EyeSlash /> : <Eye />}
                      </Button>
                    </div>

                    <div className="d-grid mt-4">
                      <Button
                        type="submit"

                        className="rounded-pill"
                        disabled={loading}
                      >
                        {loading ? (
                          <>
                            <Spinner
                              as="span"
                              animation="border"
                              size="sm"
                              role="status"
                              aria-hidden="true"
                              className="me-2"
                            />
                            Signing Up...
                          </>
                        ) : (
                          "Sign Up"
                        )}
                      </Button>
                    </div>

                    <div className="d-flex justify-content-end align-items-center gap-2 mt-3">
                      <p className="mb-0">Already have an account?</p>
                      <Button
                        variant="outline-secondary"
                        size="sm"
                        onClick={() => navigate("/login")}
                      >
                        Login
                      </Button>
                    </div>
                  </Form>
                )}
              </Formik>
            </Card>
          </motion.div>
        </Col>
      </Row>
    </Container>
  );
}
