import React from 'react';
import { Container, Row, Col } from "react-bootstrap";
import { FaFacebookF, FaLinkedinIn, FaInstagram, FaTwitter, FaPinterest } from 'react-icons/fa';

export function Footer() {
  // Define Sky Blue theme colors
  const styles = {
    footer: {
      backgroundColor: '#87CEEB', // Sky Blue
      background: 'linear-gradient(135deg, #87CEEB 0%, #B0E0E6 100%)', // Subtle gradient for a "good look"
      color: '#2C3E50',
      paddingTop: '3rem',
      paddingBottom: '1.5rem',
      boxShadow: '0 -4px 6px -1px rgba(0, 0, 0, 0.1)',
    },
    heading: {
      color: '#1a252f',
      fontWeight: '700',
      marginBottom: '1.2rem',
      textTransform: 'uppercase',
      letterSpacing: '1px',
    },
    link: {
      color: '#2C3E50',
      textDecoration: 'none',
      marginBottom: '0.5rem',
      display: 'block',
      fontWeight: '500',
      transition: 'color 0.3s ease',
    },
    socialIcon: {
      display: 'inline-flex',
      alignItems: 'center',
      justifyContent: 'center',
      width: '40px',
      height: '40px',
      backgroundColor: '#fff',
      color: '#87CEEB',
      borderRadius: '50%',
      textDecoration: 'none',
      boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
      transition: 'all 0.3s ease',
    },
    copyright: {
        borderTop: '1px solid rgba(0,0,0,0.1)',
        marginTop: '2rem',
        paddingTop: '1rem',
        fontSize: '0.9rem'
    }
  };

  return (
    <footer style={styles.footer}>
      <Container>
        <Row className="gy-4">
          {/* Brand Section */}
          <Col md={4} className="text-center text-md-start">
            <h4 style={styles.heading}>Dairyncia</h4>
            <p className="mb-3">
              Freshness delivered daily.
            </p>
          </Col>

          {/* Quick Links Section (Replaces Developed By) */}
          <Col md={4} className="text-center">
            <h5 style={styles.heading}>Quick Links</h5>
            <ul className="list-unstyled">
              <li><a href="/" style={styles.link}>Home</a></li>
              <li><a href="/products" style={styles.link}>Our Products</a></li>
              <li><a href="/about" style={styles.link}>About Us</a></li>
              <li><a href="/contact" style={styles.link}>Contact</a></li>
            </ul>
          </Col>

          {/* Social Media Section */}
          <Col md={4} className="text-center text-md-end">
            <h5 style={styles.heading}>Connect With Us</h5>
            <div className="d-flex justify-content-center justify-content-md-end gap-3 mt-3">
              <a href="#" aria-label="Facebook" style={styles.socialIcon} className="social-hover">
                <FaFacebookF size={18} />
              </a>
              <a href="https://www.instagram.com/amar.toge/" target="_blank" rel="noreferrer" aria-label="Instagram" style={styles.socialIcon} className="social-hover">
                <FaInstagram size={18} />
              </a>
              <a href="#" aria-label="Pinterest" style={styles.socialIcon} className="social-hover">
                <FaPinterest size={18} />
              </a>
              <a href="https://www.linkedin.com/in/amar-toge-062936182/" target="_blank" rel="noreferrer" aria-label="LinkedIn" style={styles.socialIcon} className="social-hover">
                <FaLinkedinIn size={18} />
              </a>
              <a href="https://x.com/indianbharatiy_" target="_blank" rel="noreferrer" aria-label="Twitter" style={styles.socialIcon} className="social-hover">
                <FaTwitter size={18} />
              </a>
            </div>
          </Col>
        </Row>

        {/* Copyright Section */}
        <Row>
          <Col className="text-center" style={styles.copyright}>
            <small>© {new Date().getFullYear()} Dairyncia. All Rights Reserved.</small>
          </Col>
        </Row>
      </Container>
    </footer>
  );
}