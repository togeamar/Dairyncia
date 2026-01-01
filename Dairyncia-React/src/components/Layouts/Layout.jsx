import { Outlet,useLocation } from "react-router-dom";
import { NavigationBar } from "./NavigationBar";
import { Footer } from "./Footer";
import { Button } from "react-bootstrap";

export function Layout() {
  const location = useLocation();
  const noNavbarRoutes = []; 

  const hideNavbar = noNavbarRoutes.includes(location.pathname);
  return (
    <>
      {!hideNavbar && <NavigationBar />}

      <main className="main-content">
        
        <Outlet />
      </main>
      <Footer />
    </>
  );
}