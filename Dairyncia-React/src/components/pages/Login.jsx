import { Button, Col, Container,Card, Form as BootstrapForm , Row,Spinner,Alert, InputGroup } from "react-bootstrap";
import * as yup from 'yup';
import { doLogin } from "../../services/userservices.js";
import { Formik,Form,Field, ErrorMessage } from "formik";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import "./Login.css";
import { Eye, EyeSlash } from "react-bootstrap-icons";
import { jwtDecode } from "jwt-decode";

import loginbg from "../../assets/images/login-background-image.png"
//import { getScore } from "../services/adminservice.js";


const LoginSchema=yup.object().shape({
    email:yup.string().email("email is invalid").required("email is required"),
    password:yup.string().required("password is required").min(6, "Password must be at least 6 characters")
    .max(16, "Password must not exceed 16 characters")
    .matches(/[a-z]/, "Password must contain at least one lowercase letter")
    .matches(/[A-Z]/, "Password must contain at least one uppercase letter")
    .matches(/[0-9]/, "Password must contain at least one number")
    .matches(/[^A-Za-z0-9]/, "Password must contain at least one special character")
});



export function Login(){
    const navigate=useNavigate();
    const [loginError, setLoginError] = useState(null);
    const [showPassword, setShowPassword] = useState(false);
    const toggleShowPassword = () => setShowPassword(!showPassword);
    

    const handleSubmit= async (values,{setSubmitting})=>{
        setLoginError(null);
        try{
            const res=await doLogin(values);
            const decoded = jwtDecode(res.data.token);
            console.log(decoded)
            localStorage.setItem("token",res.data.token);
            localStorage.setItem("type",res.data.role);
            localStorage.setItem("loggedinuser",decoded["http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name"]);
            localStorage.setItem("loggedinemail",decoded["http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress"]);
            localStorage.setItem("id",decoded["http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier"])
            if(res.data.role.toLowerCase() ==="admin") {
                navigate("/admin-dashboard")
            }
            else if( res.data.role.toLowerCase() === "manager" ){
                navigate("/manager-dashboard")
            }
            else{
                const customerData = {
                    id: res.data.id,
                    firstName: res.data.username,
                    email: res.data.email
                };
                localStorage.setItem("customer", JSON.stringify(customerData));
                navigate("/customer-dashboard");
            }
        }
        catch(error){
            let message = "Something went wrong";

            if (error.response) {
                message = error.response.data?.message || "Server error";
            } else if (error.request) {
                message = "Network error. Please check your connection.";
            } else {
                message = error.message;
            }
            console.error("Login error2:", message );
            //alert(error.response?.data?.message || "Login Failed");
            setLoginError(message );
        }
        finally{
            setSubmitting(false);
        }
    };

    return(
        <Container fluid className="mt-10">
            <Row className="justify-content-md-evenly">
                <Col  sm={5} md={4} lg={3} className="d-flex justify-content-center mr-5 align-items-center text-center">
                    <h1 className="welcome-heading mt-3">
                        “Empowering Farmers, Delivering Purity.”
                    </h1>
                </Col>
                <Col md={6} lg={4} className="mx-auto me-5">
                <Card className="login-card shadow-lg border-0 rounded-4">
                    <Card.Body>
                    <Card.Title className="login-title text-center mb-4">Login</Card.Title>

                    <Formik
                        initialValues={{ email: '', password: '' }}
                        validationSchema={LoginSchema}
                        validateOnChange={true}
                        validateOnBlur={true}
                        onSubmit={handleSubmit}
                    >
                        
                        {({ touched, errors, isSubmitting }) => (

                        <Form>
                            
                            {loginError && (
                                <Alert
                                    variant="danger" 
                                    onClose={() => setLoginError(null)} 
                                    dismissible
                                >
                                    {loginError}
                                </Alert>
                            )}
                            <BootstrapForm.Group className="mb-3" controlId="formEmail">
                            <BootstrapForm.Label>Email address</BootstrapForm.Label>
                            <Field
                                type="email"
                                name="email"
                                placeholder="Enter email"
                                as={BootstrapForm.Control}
                                isInvalid={touched.email && !!errors.email}
                            />
                            <ErrorMessage
                                name="email"
                                component="div"
                                className="invalid-feedback"
                            />
                            </BootstrapForm.Group>

                            <BootstrapForm.Group className="mb-3" controlId="formPassword">
                            <BootstrapForm.Label>Password</BootstrapForm.Label>
                            <InputGroup>
                                <Field
                                    type={showPassword ? "text" : "password"}
                                    name="password"
                                    placeholder="Password"
                                    as={BootstrapForm.Control}
                                    isInvalid={touched.password && !!errors.password}
                                />
                                <Button variant="outline-secondary" onClick={toggleShowPassword}>
                                        {showPassword ? <EyeSlash className="password-icon" /> : <Eye className="password-icon" />}
                                </Button>
                                <ErrorMessage
                                name="password"
                                component="div"
                                className="invalid-feedback"
                                />
                            </InputGroup>
                            
                            </BootstrapForm.Group>
                            <div className="d-grid">
                            <Button type="submit" className="login-btn" disabled={isSubmitting}>
                                {isSubmitting ? (
                                    <>
                                        <Spinner
                                        as="span"
                                        animation="border"
                                        size="sm"
                                        role="status"
                                        aria-hidden="true"
                                        className="me-2"
                                        />
                                        Logging in...
                                    </>
                                    ) : (
                                    'Login'
                                )}
                            </Button>
                            <div className="d-flex justify-content-end align-items-center gap-2 mx-2 mt-1">
                               <p className="mb-0">Dont have an account</p>
                               <Button className="btn-outline-secondary" size="sm" onClick={()=>{navigate("/signup")}}>Signup</Button>
                            </div>
                            </div>
                        </Form>
                        )}
                    </Formik>
                    </Card.Body>
                </Card>
                </Col>
            </Row>
        </Container>
    )
};

export default Login;