import { BrowserRouter} from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import Routes from "./routes/Routes";


import 'bootstrap/dist/css/bootstrap.min.css';


function App() {

  return (
  <BrowserRouter>
      <AuthProvider>
        <Routes />
      </AuthProvider>
    </BrowserRouter>
  
);

}

export default App
