import { useState, useEffect } from "react";
import { Navbar, Container, Nav, Offcanvas, Button } from "react-bootstrap";
import { Link, useNavigate, useLocation } from "react-router-dom";
import "./NavigationBar.css"; // Make sure to import the CSS file

export function NavigationBar() {
  const [isScrolled, setIsScrolled] = useState(false);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userRole, setUserRole] = useState(null);
  const navigate = useNavigate();
  const location = useLocation();

  
  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 20);
    };
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

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
      className={`dairyncia-navbar ${isScrolled ? "scrolled" : ""}`}
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

              {isAuthenticated && userRole.toLowerCase() === 'customer' && (
                <Nav.Link as={Link} to="/customer-dashboard" className="custom-nav-link">Dashboard</Nav.Link>
              )}
              {isAuthenticated && userRole.toLowerCase() === 'manager' && (
                <Nav.Link as={Link} to="/manager-dashboard" className="custom-nav-link">Dashboard</Nav.Link>
              )}
              {/* {isAuthenticated && userRole === 'admin' && ( */}
                <Nav.Link as={Link} to="/admin-dashboard" className="custom-nav-link">Admin</Nav.Link>
              {/* )} */}
              
              <Nav.Link as={Link} to="/services" className="custom-nav-link">Services</Nav.Link>
              <Nav.Link as={Link} to="/products" className="custom-nav-link">Products</Nav.Link> 
              <Nav.Link as={Link} to="/about" className="custom-nav-link">About</Nav.Link>
              <Nav.Link as={Link} to="/contact" className="custom-nav-link">Contact</Nav.Link>
            </Nav>

            <div className="d-flex align-items-center gap-3 mt-3 mt-lg-0">
              {(!isAuthenticated && location.pathname !== "/signup") ? (
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
              ) : ((location.pathname!=="/signup")?
                <Button 
                  onClick={handleLogout} 
                  className="btn-outline-custom"
                >
                  {(location.pathname==="/signup")?"":"Logout"}
                </Button>:""
              )}
            </div>
          </Offcanvas.Body>
        </Navbar.Offcanvas>
      </Container>
    </Navbar>
  );
}