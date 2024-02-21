import { Button, ButtonGroup, Spinner, Wrap, WrapItem } from '@chakra-ui/react';
import SidebarWithHeader from './components/shared/SideBar.jsx';
import {useEffect, useState} from 'react'
import { getCustomers } from './services/clients.js'
import CardWithImage from './components/card.jsx'

const App = () => {

    const [cusotmers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true)
        getCustomers().then(res => {
            setCustomers(res.data)
        }).catch(err => {
            console.log(err);
        }).finally(() => {
            setLoading(false);
        })
    }, [])

    if(loading){
        return(
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
        )
    }

    if (cusotmers.length <= 0) {
        return (
            <SidebarWithHeader>
                <h1>Customers List is empty</h1>
            </SidebarWithHeader>
        )
    }

    return (
        <SidebarWithHeader>
            <Wrap justify={"center"} spacing={"30px"}>
                {cusotmers.map((customer, index) => (
                    <WrapItem key={index}>
                        <CardWithImage {...customer}/>
                    </WrapItem>
                ))}
            </Wrap>
        </SidebarWithHeader>
    )
}

export default App;