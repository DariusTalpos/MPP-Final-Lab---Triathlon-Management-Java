import {useEffect, useState} from 'react'
import './App.css'
import Row from "./Row.jsx";

function App() {
  const [participants, setParticipants] = useState([]);
  const [selected, setSelected] = useState({});
  const [name, setName] = useState("");
  const [fullPoints, setFullPoints] = useState(0);


   function findAll()
   {
       let params = {
           mode: "cors"
       }
       fetch("http://localhost:8080/api/participants", params)
        .then(response => {
            if(response.ok)
            {
                return response.json();
            }
        } )
           .then(json => {
                 setParticipants(json);
           }
       ).catch(() =>
           {
               console.log("Nu-i a buna");
           }
       )
   }

   function Add()
   {
       let participant = {
           name: name,
           fullPoints: fullPoints};
       let params = {
           mode: "cors",
           method: "POST",
           body: JSON.stringify(participant),
           headers:
               {
                   "Content-Type": "application/json"
               }
       }
       fetch("http://localhost:8080/api/participants", params)
           .then( () =>
               {
                   findAll();
               }
           )
           .catch( () =>
               {
                   console.log("Nu-i a buna");
               }
           );
   }
    function Modify()
    {
        let participant = {
            id: selected.id,
            name: name,
            fullPoints: fullPoints};
        let params = {
            mode: "cors",
            method: "PUT",
            body: JSON.stringify(participant),
            headers:
                {
                    "Content-Type": "application/json"
                }
        }
        fetch("http://localhost:8080/api/participants/"+selected.id, params)
            .then( () =>
                {
                    findAll();
                }

            )
            .catch( () =>
                {
                    console.log("Nu-i a buna");
                }
            );
    }

    function Delete()
    {
        let params = {
            mode: "cors",
            method: "DELETE"
        }
        fetch("http://localhost:8080/api/participants/"+selected.id, params)
            .then( () =>
                {
                    findAll();
                }

            )
            .catch( () =>
                {








                     
                    console.log("Nu-i a buna");
                }
            );
    }


   useEffect(findAll, []);

  return (
    <>
      <h1>Triatlon Participants</h1>
      <table>
          <thead>
          <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Full Points</th>
          </tr>
          </thead>
          <tbody>
          {
              participants.map(
                  x => {
                      return (
                       <Row participant={x} setSelected={setSelected} setName={setName} setFullPoints={setFullPoints}/>
                      )
                  }
              )
          }
          </tbody>
      </table>

        <div className="fields">
            <label><pre>ID: </pre>
            <input type="number" disabled value={selected.id}/>
            </label>
            <label><pre>Name: </pre>
                <input value={name} onChange={event => setName(event.target.value)}/>
            </label>
            <label><pre>Full points: </pre>
                <input type="number" value={fullPoints} onChange={event => setFullPoints(event.target.value)}/>
            </label>
        </div>

      <div className="buttons">
        <button onClick={Add}>
          Add participant
        </button>
        <button onClick={Modify}>
            Modify participant
        </button>
        <button onClick={Delete}>
            Delete participant
        </button>
      </div>
    </>
  )
}

export default App
