export default function Row({participant, setSelected,setName,setFullPoints})
{
    function participantSelected()
    {
        setSelected(participant);
        setName(participant.name);
        setFullPoints(participant.fullPoints);
    }
    return (<tr onClick={participantSelected}>
        <td>
            {participant.id}
        </td>
        <td>
            {participant.name}
        </td>
        <td>
            {participant.fullPoints}
        </td>
    </tr>);
}