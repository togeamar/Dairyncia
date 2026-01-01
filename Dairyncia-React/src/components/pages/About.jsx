import "./About.css";
const developers = [
  {
    name: "Suyog Joshi",
    image: "Suyog.jpeg",
    linkedin: "https://linkedin.com"
  },
  {
    name: "Pradip Patil",
    image: "Pradip.jpeg",
    linkedin: "https://linkedin.com"
  },
  {
    name: "Yash Bambal",
    image: "YashBambalImg.JPG",
    linkedin: "https://linkedin.com"
  },
  {
    name: "Amar Toge",
    image: "https://via.placeholder.com/150",
    linkedin: "https://linkedin.com"
  },
  {
    name: "Someshwar Tiwari",
    image: "Someshvar.jpeg",
    linkedin: "https://linkedin.com"
  }
];

function About() {
  return (
    <div className="about-page container my-5">
      <h2 className="text-center mb-4">About Dairyencia</h2>

      <p className="text-center">
        Dairyencia is a smart dairy management platform designed to
        digitalize dairy operations such as milk collection, farmer management,
        payments, and reporting. Our aim is to empower farmers and dairy owners
        with transparency and efficiency.
      </p>

      <h4 className="mt-5">Our Motive</h4>
      <p>
        Our motive is to eliminate manual records, reduce errors,
        and bring a single digital solution for dairy businesses
        to grow sustainably.
      </p>

      <h4 className="mt-5 text-center">Meet the Developers</h4>

      <div className="row mt-4 justify-content-center">
        {developers.map((dev, index) => (
          <div className="col-md-3 col-lg-2 mb-4" key={index}>
            <div className="card h-100 text-center shadow-sm">
              <img
                src={dev.image}
                className="card-img-top dev-image"
                alt={dev.name}
              />
              <div className="card-body">
                <h6 className="card-title">{dev.name}</h6>
                <a
                  href={dev.linkedin}
                  target="_blank"
                  rel="noreferrer"
                  className="text-decoration-none"
                >
                  LinkedIn
                </a>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default About;
