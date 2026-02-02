import { useState, useEffect } from "react";
import { Navbar, Container, Nav, Offcanvas, Button } from "react-bootstrap";
import { Link, useNavigate, useLocation } from "react-router-dom";
import "./NavigationBar.css";

export function NavigationBar() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userRole, setUserRole] = useState(null);
  const navigate = useNavigate();
  const location = useLocation();
  const pages=["/login","/signup"];

  useEffect(() => {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("type");
    setIsAuthenticated(!!token);
    setUserRole(role);
  }, [location]);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("type");
    localStorage.removeItem("loggedinuser");
    localStorage.removeItem("loggedinemail");
    setIsAuthenticated(false);
    setUserRole(null);
    navigate("/login");
  };

  return (
    <Navbar
      expand="lg"
      fixed="top"
      className="dairyncia-navbar"
    >
      <Container>
        <Navbar.Brand as={Link} to="/" className="brand-logo">
          Dairyncia<span className="dot">.</span>
        </Navbar.Brand>

        <Navbar.Toggle aria-controls="offcanvasNavbar" className="custom-toggler" />
        
        <Navbar.Offcanvas
          id="offcanvasNavbar"
          aria-labelledby="offcanvasNavbarLabel"
          placement="end"
          className="dairyncia-offcanvas"
        >
          <Offcanvas.Header closeButton>
            <Offcanvas.Title id="offcanvasNavbarLabel" className="brand-logo mobile">
              Dairyncia
            </Offcanvas.Title>
          </Offcanvas.Header>

          <Offcanvas.Body>
            <Nav className="justify-content-center flex-grow-1 pe-3 nav-links-container">
              <Nav.Link as={Link} to="/" className="custom-nav-link">Home</Nav.Link>

              {isAuthenticated && userRole.toLowerCase() === 'farmer' && (
                <Nav.Link as={Link} to="/farmer-dashboard" className="custom-nav-link">Dashboard</Nav.Link>
              )}
              {isAuthenticated && userRole.toLowerCase() === 'manager' && (
                <Nav.Link as={Link} to="/manager-dashboard" className="custom-nav-link">Dashboard</Nav.Link>
              )}
              { isAuthenticated && userRole.toLowerCase() === 'admin' && ( 
                <Nav.Link as={Link} to="/admin-dashboard" className="custom-nav-link">Dashboard</Nav.Link>
               )} 
              
              <Nav.Link as={Link} to="/about" className="custom-nav-link">About</Nav.Link>
              <Nav.Link as={Link} to="/contact" className="custom-nav-link">Contact</Nav.Link>
            </Nav>

            <div className="d-flex align-items-center gap-3 mt-3 mt-lg-0">
              {(!isAuthenticated && !pages.includes(location.pathname)) ? (
                <>
                <Button 
                  as={Link} 
                  to="/login" 
                  className="btn-primary-custom"
                >
                  Login
                </Button>
                <Button 
                  as={Link}
                  to="/signup"
                  className="btn-primary-custom">
                    SignUp
                </Button>
                </>
              ) : ((!pages.includes(location.pathname))?
                <Button 
                  onClick={handleLogout} 
                  className="btn-outline-custom"
                >
                  Logout
                </Button>:""
              )}
            </div>
          </Offcanvas.Body>
        </Navbar.Offcanvas>
      </Container>
    </Navbar>
  );
}