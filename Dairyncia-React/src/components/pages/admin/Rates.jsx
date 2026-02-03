import { useEffect, useState } from "react";
import { Container,Alert } from "react-bootstrap";
import { loadRates, uploadfFile } from "../../../services/adminservice";
import "./Rates.css"

export default function Rates(){
    const [file,setFile]=useState(null);
    const [milkType, setMilkType] = useState(1);
    const [rates,setRates]=useState({ data: [] });
    const [ratesb,setRatesB]=useState({ data: [] });
    const [error,setError]=useState("");
    const [loading,setLoading]=useState(true);
    const [pageNumberCows,setPageNumberCows]=useState(1);
    const [pageNumberBuff,setPageNumberBuff]=useState(1);
    const [fileLoading,setFileLoading]=useState(false);
    const [successMsg, setSuccessMsg] = useState("");

    useEffect(()=>{
        const loadRatesCows=async()=>{
            try{
                const response=await loadRates({milkType:1,pageNumber:pageNumberCows})
                setRates(response.data);
                console.log(response.data);
            }
            catch(err){
                setError("failed to load cow milk rates");
            }
            finally{
                setLoading(false)
            }
        }
        loadRatesCows();
    },[pageNumberCows]);

    useEffect(()=>{
        const loadRatesBuffalow=async()=>{
            try{
                const response=await loadRates({milkType:2,pageNumber:pageNumberBuff})
                setRatesB(response.data);
                console.log(response.data);
            }
            catch(err){
                setError("failed to load buffalow milk rates");
            }
            finally{
                setLoading(false)
            }
        }
        loadRatesBuffalow();
    },[pageNumberBuff]);

    function handlecowdec() {
        if(pageNumberCows>1)
            setPageNumberCows(pageNumberCows-1)
    }
    function handlecowinc() {
        setPageNumberCows(pageNumberCows+1)
    }
    function handlebuffdec() {
        if(pageNumberBuff>1)
            setPageNumberBuff(pageNumberBuff-1)
    }
    function handlebuffinc() {
        setPageNumberBuff(pageNumberBuff+1)
    }


    function onfilechange(e){
        setFile(e.target.files[0]);
    }

    const handleupload=async()=>{
        setFileLoading(true);
        if(!file){
            setFileLoading(false);
            return alert("please select a file");}
        const formData=new FormData();

        formData.append("file",file);

        try{
            const response=await uploadfFile(formData,milkType)
            setSuccessMsg("File uploaded and "+response.data.message);
            console.log(response.data)
            setFile(null);
            document.querySelector('input[type="file"]').value = "";
            setTimeout(() => setSuccessMsg(""), 3000);
        }
        catch(err){
            setError("Upload failed: " + (err.response?.data || err.message));
        }
        finally{
            setFileLoading(false);
        }
    }

    if(loading){
        return <div>Loading...</div>;
    }
    return (
        <Container >
            <h2>Cow Milk Rates</h2>
             <table className="table table-hover">
                <thead className="thead">
                <tr>
                    <th scope="col">SNF</th>
                    <th scope="col">FAT</th>
                    <th scope="col">Rate</th>
                </tr>
               </thead>
            </table>
            <div className="table-wrapper">
           
            <table className="table table-hover">
               
               <tbody>
                {rates.data.map((x)=>(
                    <tr>
                        <td>{x.snf}</td>
                        <td>{x.fat}</td>
                        <td>{x.rate}</td>
                    </tr>
                )
            )}
               </tbody>

            </table>
            </div>
            <div className="d-flex mt-2 justify-content-between">
                <button type="button" className="btn btn-outline-info" onClick={handlecowdec}>previous page</button>
                <button type="button" className="btn btn-outline-info" onClick={handlecowinc}>next page</button>

            </div>    
            <h2 className="mt-2">Buffalow Milk Rates</h2>
            <table className="table table-hover">
                <thead className="thead">
                <tr>
                    <th scope="col">SNF</th>
                    <th scope="col">FAT</th>
                    <th scope="col">Rate</th>
                </tr>
               </thead>
            </table>
            <div className="table-wrapper">
           
            <table className="table table-hover">
               
               <tbody>
                {ratesb.data.length===0?(
                        <tr>
                            <td colSpan={3} style={{ textAlign: "center" }}>
                            No Data Found
                            </td>
                        </tr>
                        ):ratesb.data.map((x)=>(
                    <tr>
                        <td>{x.snf}</td>
                        <td>{x.fat}</td>
                        <td>{x.rate}</td>
                    </tr>
                )
            )}
               </tbody>

            </table>
            </div>
            <div className="d-flex mt-2 justify-content-between">
                <button type="button" className="btn btn-outline-info" onClick={handlebuffdec}>previous page</button>
                <button type="button" className="btn btn-outline-info" onClick={handlebuffinc}>next page</button>

            </div>  
            
            
            <div className="mt-2 p-4 border rounded shadow">
                <h3>Upload Milk Rate Chart</h3>
                
                <div className="mb-3">
                    <label>Milk Type: </label>
                    <select value={milkType} onChange={(e) => setMilkType(e.target.value)}>
                        <option value="1">Cow</option>
                        <option value="2">Buffalo</option>
                    </select>
                </div>

                {/* ✅ SHOW SUCCESS MESSAGE */}
                {successMsg && <Alert variant="success">{successMsg}</Alert>}

                {/* ✅ SHOW ERROR MESSAGE (If you haven't already) */}
                {error && <Alert variant="danger">{error}</Alert>}

                <div className="mb-3">
                    <input type="file" accept=".xlsx, .xls" onChange={onfilechange} />
                </div>

                <button onClick={handleupload} disabled={fileLoading} className="btn btn-primary">
                    
                    {fileLoading?"Loading...":"Upload & Process"}
                </button>
             </div>
        </Container>
    );
}