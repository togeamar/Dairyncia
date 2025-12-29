import { Outlet } from "react-router-dom";
import { NavigationBar } from "./NavigationBar";
import { Footer } from "./Footer";

export function Layout() {
  return (
    <>
      <NavigationBar />
      <main className="main-content">
        <Outlet />
      </main>
      <Footer />
    </>
  );
}