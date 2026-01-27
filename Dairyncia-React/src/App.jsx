import { Home } from "./components/pages/Home";
import { Layout } from './components/Layouts/Layout';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import  About  from "./components/pages/About";
import Contact from "./components/pages/Contact";
import Admin from "../src/components/pages/Admin";
import { Signup } from './components/pages/SignUp';
import { Login } from './components/pages/Login';
import Manager from './components/pages/Manager';
import ProtectedRoute from "./components/pages/ProtectedRoute";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Home />} />
          <Route path="about" element={<About />} />
          <Route path="contact" element={<Contact />} />
          <Route element={<ProtectedRoute allowedRoles={"Admin"}/>}>
            <Route path="admin-dashboard" element={<Admin />} />
          </Route>
          <Route path="login" element={<Login />} />
          <Route path="signup" element={<Signup />} />
          <Route element={<ProtectedRoute allowedRoles={"Manager"}/>}>
            <Route path="manager-dashboard" element={<Manager />} />
          </Route>
          <Route element={<ProtectedRoute allowedRoles={"Farmer"} />}>
  <Route path="farmer-dashboard" element={<FarmerDashboard />} />
</Route>

        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
