import React from 'react';
import { Container, Row, Col } from "react-bootstrap";
import { FaFacebookF, FaLinkedinIn, FaInstagram, FaTwitter, FaPinterest, FaEnvelope, FaPhone, FaMapMarkerAlt, FaHeart } from 'react-icons/fa';

export function Footer() {
  const styles = {
    footer: {
      background: 'linear-gradient(135deg, #1b2d4f 0%, #106678 100%)',
      color: '#ffffff',
      position: 'relative',
      overflow: 'hidden',
    },
    footerOverlay: {
      position: 'absolute',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      background: 'rgba(0,0,0,0.1)',
      zIndex: 1,
    },
    footerContent: {
      position: 'relative',
      zIndex: 2,
      paddingTop: '4rem',
      paddingBottom: '2rem',
    },
    waveTop: {
      position: 'absolute',
      top: 0,
      left: 0,
      width: '100%',
      height: '100px',
      background: 'url("data:image/svg+xml,%3Csvg xmlns=\'http://www.w3.org/2000/svg\' viewBox=\'0 0 1200 120\' preserveAspectRatio=\'none\'%3E%3Cpath d=\'M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V0Z\' opacity=\'.25\' fill=\'%23ffffff\'/%3E%3Cpath d=\'M0,0V15.81C13,36.92,27.64,56.86,47.69,72.05,99.41,111.27,165,111,224.58,91.58c31.15-10.15,60.09-26.07,89.67-39.8,40.92-19,84.73-46,130.83-49.67,36.26-2.85,70.9,9.42,98.6,31.56,31.77,25.39,62.32,62,103.63,73,40.44,10.79,81.35-6.69,119.13-24.28s75.16-39,116.92-43.05c59.73-5.85,113.28,22.88,168.9,38.84,30.2,8.66,59,6.17,87.09-7.5,22.43-10.89,48-26.93,60.65-49.24V0Z\' opacity=\'.5\' fill=\'%23ffffff\'/%3E%3Cpath d=\'M0,0V5.63C149.93,59,314.09,71.32,475.83,42.57c43-7.64,84.23-20.12,127.61-26.46,59-8.63,112.48,12.24,165.56,35.4C827.93,77.22,886,95.24,951.2,90c86.53-7,172.46-45.71,248.8-84.81V0Z\' fill=\'%23ffffff\'/%3E%3C/svg%3E") no-repeat',
      backgroundSize: 'cover',
      zIndex: 0,
    },
    heading: {
      color: '#ffffff',
      fontWeight: '700',
      marginBottom: '1.5rem',
      fontSize: '1.3rem',
      textTransform: 'uppercase',
      letterSpacing: '1px',
      position: 'relative',
    },
    headingUnderline: {
      content: '""',
      position: 'absolute',
      bottom: '-8px',
      left: 0,
      width: '50px',
      height: '3px',
      background: 'linear-gradient(90deg, #FFD700, #FFA500)',
      borderRadius: '2px',
    },
    link: {
      color: 'rgba(255,255,255,0.8)',
      textDecoration: 'none',
      marginBottom: '0.8rem',
      display: 'block',
      fontWeight: '400',
      transition: 'all 0.3s ease',
      padding: '5px 0',
    },
    contactItem: {
      display: 'flex',
      alignItems: 'center',
      marginBottom: '1rem',
      color: 'rgba(255,255,255,0.9)',
    },
    contactIcon: {
      marginRight: '12px',
      fontSize: '1.1rem',
      color: '#FFD700',
    },
    socialContainer: {
      display: 'flex',
      gap: '15px',
      justifyContent: 'center',
      marginTop: '1.5rem',
    },
    socialIcon: {
      display: 'inline-flex',
      alignItems: 'center',
      justifyContent: 'center',
      width: '50px',
      height: '50px',
      background: 'linear-gradient(135deg, rgba(255,255,255,0.1), rgba(255,255,255,0.05))',
      color: '#ffffff',
      borderRadius: '50%',
      textDecoration: 'none',
      border: '2px solid rgba(255,255,255,0.2)',
      transition: 'all 0.4s ease',
      backdropFilter: 'blur(10px)',
    },
    copyright: {
      borderTop: '1px solid rgba(255,255,255,0.2)',
      marginTop: '3rem',
      paddingTop: '2rem',
      paddingBottom: '1rem',
      fontSize: '0.9rem',
      textAlign: 'center',
      background: 'rgba(0,0,0,0.1)',
      marginLeft: '-15px',
      marginRight: '-15px',
      paddingLeft: '15px',
      paddingRight: '15px',
    },
    brandSection: {
      textAlign: 'center',
    },
    brandLogo: {
      fontSize: '2.5rem',
      fontWeight: '800',
      marginBottom: '1rem',
      background: 'linear-gradient(45deg, #FFD700, #FFA500)',
      WebkitBackgroundClip: 'text',
      WebkitTextFillColor: 'transparent',
      backgroundClip: 'text',
    },
    brandDescription: {
      fontSize: '1.1rem',
      lineHeight: '1.6',
      marginBottom: '1.5rem',
      color: 'rgba(255,255,255,0.9)',
    },
    heartIcon: {
      color: '#FF6B6B',
      animation: 'heartbeat 1.5s ease-in-out infinite',
    }
  };

  return (
    <footer style={styles.footer}>
      <div style={styles.waveTop}></div>
      <div style={styles.footerOverlay}></div>
      
      <Container style={styles.footerContent}>
        <Row className="gy-4">
          {/* Brand Section */}
          <Col lg={4} md={6} style={styles.brandSection}>
            <h2 style={styles.brandLogo}>Dairyncia</h2>
            <p style={styles.brandDescription}>
              Revolutionizing dairy management with cutting-edge technology. 
              Connecting farmers, managers, and the future of fresh dairy products.
            </p>
            <div style={styles.socialContainer}>
              <a href="#" aria-label="Facebook" style={styles.socialIcon} 
                 onMouseEnter={(e) => {
                   e.target.style.transform = 'translateY(-5px) scale(1.1)';
                   e.target.style.background = 'linear-gradient(135deg, #3b5998, #8b9dc3)';
                 }}
                 onMouseLeave={(e) => {
                   e.target.style.transform = 'translateY(0) scale(1)';
                   e.target.style.background = 'linear-gradient(135deg, rgba(255,255,255,0.1), rgba(255,255,255,0.05))';
                 }}>
                <FaFacebookF size={20} />
              </a>
              <a href="https://www.instagram.com/amar.toge/" target="_blank" rel="noreferrer" 
                 aria-label="Instagram" style={styles.socialIcon}
                 onMouseEnter={(e) => {
                   e.target.style.transform = 'translateY(-5px) scale(1.1)';
                   e.target.style.background = 'linear-gradient(135deg, #e4405f, #f093fb)';
                 }}
                 onMouseLeave={(e) => {
                   e.target.style.transform = 'translateY(0) scale(1)';
                   e.target.style.background = 'linear-gradient(135deg, rgba(255,255,255,0.1), rgba(255,255,255,0.05))';
                 }}>
                <FaInstagram size={20} />
              </a>
              <a href="#" aria-label="Pinterest" style={styles.socialIcon}
                 onMouseEnter={(e) => {
                   e.target.style.transform = 'translateY(-5px) scale(1.1)';
                   e.target.style.background = 'linear-gradient(135deg, #bd081c, #e60023)';
                 }}
                 onMouseLeave={(e) => {
                   e.target.style.transform = 'translateY(0) scale(1)';
                   e.target.style.background = 'linear-gradient(135deg, rgba(255,255,255,0.1), rgba(255,255,255,0.05))';
                 }}>
                <FaPinterest size={20} />
              </a>
              <a href="https://www.linkedin.com/in/amar-toge-062936182/" target="_blank" rel="noreferrer" 
                 aria-label="LinkedIn" style={styles.socialIcon}
                 onMouseEnter={(e) => {
                   e.target.style.transform = 'translateY(-5px) scale(1.1)';
                   e.target.style.background = 'linear-gradient(135deg, #0077b5, #00a0dc)';
                 }}
                 onMouseLeave={(e) => {
                   e.target.style.transform = 'translateY(0) scale(1)';
                   e.target.style.background = 'linear-gradient(135deg, rgba(255,255,255,0.1), rgba(255,255,255,0.05))';
                 }}>
                <FaLinkedinIn size={20} />
              </a>
              <a href="https://x.com/indianbharatiy_" target="_blank" rel="noreferrer" 
                 aria-label="Twitter" style={styles.socialIcon}
                 onMouseEnter={(e) => {
                   e.target.style.transform = 'translateY(-5px) scale(1.1)';
                   e.target.style.background = 'linear-gradient(135deg, #1da1f2, #0d8bd9)';
                 }}
                 onMouseLeave={(e) => {
                   e.target.style.transform = 'translateY(0) scale(1)';
                   e.target.style.background = 'linear-gradient(135deg, rgba(255,255,255,0.1), rgba(255,255,255,0.05))';
                 }}>
                <FaTwitter size={20} />
              </a>
            </div>
          </Col>

          {/* Quick Links Section */}
          <Col lg={2} md={6}>
            <h5 style={styles.heading}>
              Quick Links
              <div style={styles.headingUnderline}></div>
            </h5>
            <ul className="list-unstyled">
              <li><a href="/" style={styles.link}
                     onMouseEnter={(e) => {
                       e.target.style.color = '#FFD700';
                       e.target.style.paddingLeft = '10px';
                     }}
                     onMouseLeave={(e) => {
                       e.target.style.color = 'rgba(255,255,255,0.8)';
                       e.target.style.paddingLeft = '0px';
                     }}>Home</a></li>
              <li><a href="/about" style={styles.link}
                     onMouseEnter={(e) => {
                       e.target.style.color = '#FFD700';
                       e.target.style.paddingLeft = '10px';
                     }}
                     onMouseLeave={(e) => {
                       e.target.style.color = 'rgba(255,255,255,0.8)';
                       e.target.style.paddingLeft = '0px';
                     }}>About Us</a></li>
              <li><a href="/contact" style={styles.link}
                     onMouseEnter={(e) => {
                       e.target.style.color = '#FFD700';
                       e.target.style.paddingLeft = '10px';
                     }}
                     onMouseLeave={(e) => {
                       e.target.style.color = 'rgba(255,255,255,0.8)';
                       e.target.style.paddingLeft = '0px';
                     }}>Contact</a></li>
            </ul>
          </Col>

          {/* Services Section */}
          <Col lg={3} md={6}>
            <h5 style={styles.heading}>
              Our Services
              <div style={styles.headingUnderline}></div>
            </h5>
            <ul className="list-unstyled">
              <li><a href="#" style={styles.link}
                     onMouseEnter={(e) => {
                       e.target.style.color = '#FFD700';
                       e.target.style.paddingLeft = '10px';
                     }}
                     onMouseLeave={(e) => {
                       e.target.style.color = 'rgba(255,255,255,0.8)';
                       e.target.style.paddingLeft = '0px';
                     }}>Milk Collection</a></li>
              <li><a href="#" style={styles.link}
                     onMouseEnter={(e) => {
                       e.target.style.color = '#FFD700';
                       e.target.style.paddingLeft = '10px';
                     }}
                     onMouseLeave={(e) => {
                       e.target.style.color = 'rgba(255,255,255,0.8)';
                       e.target.style.paddingLeft = '0px';
                     }}>Quality Testing</a></li>
              <li><a href="#" style={styles.link}
                     onMouseEnter={(e) => {
                       e.target.style.color = '#FFD700';
                       e.target.style.paddingLeft = '10px';
                     }}
                     onMouseLeave={(e) => {
                       e.target.style.color = 'rgba(255,255,255,0.8)';
                       e.target.style.paddingLeft = '0px';
                     }}>Farmer Management</a></li>
              <li><a href="#" style={styles.link}
                     onMouseEnter={(e) => {
                       e.target.style.color = '#FFD700';
                       e.target.style.paddingLeft = '10px';
                     }}
                     onMouseLeave={(e) => {
                       e.target.style.color = 'rgba(255,255,255,0.8)';
                       e.target.style.paddingLeft = '0px';
                     }}>Payment Processing</a></li>
            </ul>
          </Col>

          {/* Contact Section */}
          <Col lg={3} md={6}>
            <h5 style={styles.heading}>
              Get In Touch
              <div style={styles.headingUnderline}></div>
            </h5>
            <div style={styles.contactItem}>
              <FaMapMarkerAlt style={styles.contactIcon} />
              <span>123 Dairy Street, Farm City, FC 12345</span>
            </div>
            <div style={styles.contactItem}>
              <FaPhone style={styles.contactIcon} />
              <span>+1 (555) 123-4567</span>
            </div>
            <div style={styles.contactItem}>
              <FaEnvelope style={styles.contactIcon} />
              <span>info@dairyncia.com</span>
            </div>
          </Col>
        </Row>

        {/* Copyright Section */}
        <Row>
          <Col style={styles.copyright}>
            <p className="mb-0">
              © {new Date().getFullYear()} Dairyncia. All Rights Reserved. | 
              Made by the Dairyncia Team
            </p>
          </Col>
        </Row>
      </Container>
      
      <style jsx>{`
        @keyframes heartbeat {
          0%, 100% { transform: scale(1); }
          50% { transform: scale(1.2); }
        }
      `}</style>
    </footer>
  );
}