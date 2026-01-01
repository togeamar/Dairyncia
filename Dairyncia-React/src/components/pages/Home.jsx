import React, { useEffect, useState } from 'react';
import { Droplet, Users, TrendingUp, Truck, ChevronRight } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { Button, Carousel, Container, Row, Col, Card, Badge } from "react-bootstrap";
import "./Home.css";

export function Home() {

  const [loggedinUser,setLoggedInUser]=useState(localStorage.getItem("loggedinuser"));


  return (
    <>     

      <div className="dairyncia-wrapper">
        
        {/* Hero Section */}
        <header className="hero-section text-center">
          <div className="container">
            <div className="row justify-content-center">
              <div className="col-lg-8">
                <div className="mb-4">
                  <Droplet size={64} className="text-white mb-3" />
                </div>
                <h1 className="hero-title mb-3">Welcome to Dairyncia{loggedinUser?`, ${loggedinUser}`:""}</h1>
                <p className="hero-subtitle mb-5">
                  The ultimate solution for efficient milk collection and management. 
                  Bridge the gap between local farmers and major milk companies with precision and trust.
                </p>
                <div className="d-flex gap-3 justify-content-center">
                  <button className="btn btn-custom-light">
                    Manage Collection
                  </button>
                  <button className="btn btn-outline-light rounded-pill px-4 py-2">
                    View Daily Rates
                  </button>
                </div>
              </div>
            </div>
          </div>
        </header>

        {/* Main Content Area */}
        <div className="container">
          
          {/* Quick Stats / Dashboard Preview */}
          <div className="row mb-5 g-4">
            <div className="col-md-4">
              <div className="p-4 stat-card shadow-sm h-100">
                <h5 className="text-muted text-uppercase fs-6 fw-bold">Daily Collection</h5>
                <h2 className="fw-bold text-primary">2,450 Liters</h2>
                <p className="mb-0 text-success small"><TrendingUp size={16} className="me-1"/> +12% from yesterday</p>
              </div>
            </div>
            <div className="col-md-4">
              <div className="p-4 stat-card shadow-sm h-100" style={{borderLeftColor: '#198754'}}>
                <h5 className="text-muted text-uppercase fs-6 fw-bold">Active Farmers</h5>
                <h2 className="fw-bold text-success">142</h2>
                <p className="mb-0 text-muted small">Registered Suppliers</p>
              </div>
            </div>
            <div className="col-md-4">
              <div className="p-4 stat-card shadow-sm h-100" style={{borderLeftColor: '#0dcaf0'}}>
                <h5 className="text-muted text-uppercase fs-6 fw-bold">Market Rate</h5>
                <h2 className="fw-bold text-info">₹32.50 / L</h2>
                <p className="mb-0 text-muted small">Current Buying Price</p>
              </div>
            </div>
          </div>

          {/* Core Services */}
          <div className="row mb-5">
            <div className="col-12 text-center mb-5">
              <h2 className="fw-bold">Our Management Services</h2>
              <p className="text-muted">Streamlining operations for the modern Dairy Manager</p>
            </div>

            <div className="col-md-6 col-lg-4 mb-4">
              <div className="feature-card text-center p-4">
                <div className="icon-wrapper bg-soft-blue mx-auto">
                  <Users size={32} />
                </div>
                <h4 className="mb-3">Farmer Acquisition</h4>
                <p className="text-muted">
                  Seamlessly register local farmers, manage their profiles, and track daily milk contributions individually.
                </p>
                <a href="#" className="text-decoration-none fw-bold">Manage Farmers <ChevronRight size={16}/></a>
              </div>
            </div>

            <div className="col-md-6 col-lg-4 mb-4">
              <div className="feature-card text-center p-4">
                <div className="icon-wrapper bg-soft-green mx-auto">
                  <Droplet size={32} />
                </div>
                <h4 className="mb-3">Quality Control</h4>
                <p className="text-muted">
                  Record fat content and SNF levels for every batch. Ensure high-quality standards before selling to big companies.
                </p>
                <a href="#" className="text-decoration-none fw-bold text-success">Check Quality <ChevronRight size={16}/></a>
              </div>
            </div>

            <div className="col-md-6 col-lg-4 mb-4">
              <div className="feature-card text-center p-4">
                <div className="icon-wrapper bg-soft-info mx-auto">
                  <Truck size={32} />
                </div>
                <h4 className="mb-3">Commercial Sales</h4>
                <p className="text-muted">
                  Consolidate collections and manage bulk sales to major dairy corporations with automated invoicing.
                </p>
                <a href="#" className="text-decoration-none fw-bold text-info">View Sales <ChevronRight size={16}/></a>
              </div>
            </div>
          </div>
        </div>

        {/* CTA Section */}
        <section className="cta-section text-center border-top">
          <div className="container">
            <h2 className="fw-bold mb-3">Ready to optimize your collection?</h2>
            <p className="text-muted mb-4">Join hundreds of collection centers using Dairyncia.</p>
            <button className="btn btn-primary btn-lg rounded-pill px-5">Start Collection Entry</button>
          </div>
        </section>

      </div>
    </>
  );
};

