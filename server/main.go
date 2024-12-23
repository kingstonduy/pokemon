package main

import (
	"fmt"
	"net"
	"os"
	"strings"
	"time"
)

type ClientInfo struct {
	IP   string
	Port int
	X   string
	Y   string
}

var clients = make(map[string]ClientInfo)

func main() {
	service := ":1200"
	udpAddr, err := net.ResolveUDPAddr("udp4", service)
	checkError(err)
	conn, err := net.ListenUDP("udp", udpAddr)
	checkError(err)
	for {
		handleClient(conn)
	}
}

func handleClient(conn *net.UDPConn) {
	var buf [512]byte
	n, addr, err := conn.ReadFromUDP(buf[0:])
	if err != nil {
		return
	}
	message := string(buf[:n])
	// fmt.Println("Received: ", message)
	// fmt.Println("Client IP: ", addr.IP.String())
	// fmt.Println("Client Port: ", addr.Port)

	parts := strings.Split(message, " ")

	ClientIP := addr.IP.String()
	ClientPort := addr.Port
	// fmt.Println(addr.String())
	if parts[0] == "where" {
		playerX := parts[1]
		playerY := parts[2]
		keyCodeUp := parts[3]
		keyCodeDown := parts[4]
		clients[addr.String()] = ClientInfo{IP: ClientIP, Port: ClientPort, X: playerX, Y: playerY}
		for clientAddr := range clients {
			if clientAddr != addr.String() {
				message := "where " + playerX + " " + playerY + " " + keyCodeUp + " " + keyCodeDown + " " + addr.String()
				udpClientAddr, _ := net.ResolveUDPAddr("udp", clientAddr)
				_, err := conn.WriteToUDP([]byte(message), udpClientAddr)
				if err != nil {
					fmt.Println("Error: ", err)
				}
			}
		
		}

	} else if parts[0] == "register" {
		
		playerX := parts[1]
		playerY := parts[2]
		fmt.Println("Player X: ", playerX)
		fmt.Println("Player Y: ", playerY)
		

		clients[addr.String()] = ClientInfo{IP: ClientIP, Port: ClientPort, X: playerX, Y: playerY}

		returnMessage := "registered " + addr.String();
		_, err := conn.WriteToUDP([]byte(returnMessage), addr)
		if err != nil {
			fmt.Println("Error: ", err)
		}

		time.Sleep(2 * time.Second)

		for clientAddr := range clients {
			if clientAddr != addr.String() {
				message := "newplayer " + playerX + " " + playerY + " " + clientAddr + " " + addr.String()
				udpClientAddr, _ := net.ResolveUDPAddr("udp", clientAddr)
				_, err := conn.WriteToUDP([]byte(message), udpClientAddr)
				if err != nil {
					fmt.Println("Error: ", err)
				}
			}
		}

		for clientAddr := range clients {
			if clientAddr != addr.String() {
				message := "newplayer " + clients[clientAddr].X + " " + clients[clientAddr].Y + " " + addr.String() + " " + clientAddr
				udpClientAddr, _ := net.ResolveUDPAddr("udp", addr.String())
				_, err := conn.WriteToUDP([]byte(message), udpClientAddr)
				if err != nil {
					fmt.Println("Error: ", err)
				}
			}
		}

	} else if parts[0] == "battle" {
		opponentAddr := parts[1]
		message := "ask " + addr.String()
		udpClientAddr, _ := net.ResolveUDPAddr("udp", opponentAddr)
		_, err := conn.WriteToUDP([]byte(message), udpClientAddr)
		if err != nil {
			fmt.Println("Error: ", err)
		}
	} else if parts[0] == "responsebattle" {
		oppenentAddr := parts[1]
		response := parts[2]
		message := "responsebattle " + addr.String() + " " + response
		udpClientAddr, _ := net.ResolveUDPAddr("udp", oppenentAddr)
		_, err := conn.WriteToUDP([]byte(message), udpClientAddr)
		if err != nil {
			fmt.Println("Error: ", err)
		}
	} else if parts[0] == "pokemon" {
		opponentAddr := parts[1]
		name := parts[2]
		level := parts[3]
		exp := parts[4]
		hp := parts[5]
		attack := parts[6]
		defense := parts[7]
		special_attack := parts[8]
		special_defense := parts[9]
		speed := parts[10]
		message := "pokemon " + addr.String() + " " + name + " " + level + " " + exp + " " + hp + " " + attack + " " + defense + " " + special_attack + " " + special_defense + " " + speed
		udpClientAddr, _ := net.ResolveUDPAddr("udp", opponentAddr)
		_, err := conn.WriteToUDP([]byte(message), udpClientAddr)
		if err != nil {
			fmt.Println("Error: ", err)
		}
	} else if parts[0] == "input" {
		opponentAddr := parts[2]
		input := parts[1]
		message := "input " + addr.String() + " " + input
		udpClientAddr, _ := net.ResolveUDPAddr("udp", opponentAddr)
		_, err := conn.WriteToUDP([]byte(message), udpClientAddr)
		if err != nil {
			fmt.Println("Error: ", err)
		}
	} else if parts[0] == "damage" {
		opponentAddr := parts[2]
		damage := parts[1]
		message := "damage " + addr.String() + " " + damage
		udpClientAddr, _ := net.ResolveUDPAddr("udp", opponentAddr)
		_, err := conn.WriteToUDP([]byte(message), udpClientAddr)
		if err != nil {
			fmt.Println("Error: ", err)
		}
	}
}

func checkError(err error) {
	if err != nil {
		_, err := fmt.Fprintf(os.Stderr, "Fatal error ", err.Error())
		if err != nil {
			return
		}
		os.Exit(1)
	}
}